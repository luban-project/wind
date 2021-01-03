package com.lvonce.wind.factory;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.lvonce.wind.RestFunction;
import com.lvonce.wind.RestRouter;
import com.lvonce.wind.SQLDataSourceManager;
import com.lvonce.wind.compiler.CompilerOfJavaString;
import com.lvonce.wind.util.YamlUtil;
import com.lvonce.wind.watcher.DefaultDirHandler;
import com.lvonce.wind.watcher.DefaultFileHandler;
import com.lvonce.wind.watcher.DirWatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
public class RestFunctionFactoryHot implements RestFunctionFactory {

    private final Map<String, Supplier<RestFunction>> supplierMap = new LinkedHashMap<>();

    private SQLDataSourceManager sqlDataSourceManager;

    private RestRouter restRouter;

    private final DirWatcher dirWatcher = new DirWatcher();

    private final CompilerOfJavaString compiler = new CompilerOfJavaString();

    private Injector injector;

    public void registerModule(Iterable<Class<?>> moduleClasses) {
        try {
            List<AbstractModule> modules = new ArrayList<>();
            for (Class<?> moduleClass : moduleClasses) {
                AbstractModule factoryModule = (AbstractModule) moduleClass.newInstance();
                modules.add(factoryModule);
                log.info("add module by name {}", moduleClass.getCanonicalName());
                System.out.println(String.format("add module by name %s", moduleClass.getCanonicalName()));
            }
            modules.add(new ConfigurationLoaderModule());
            this.injector = Guice.createInjector(modules);
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
        }
    }

    private void registerClass(Iterable<Class<?>> classes) {
        try {
            for (Class<?> clazz : classes) {
                Supplier<RestFunction> supplier = () -> (RestFunction) injector.getInstance(clazz);
                this.supplierMap.put(clazz.getCanonicalName(), supplier);
                log.info("add supplier by name: {}", clazz.getCanonicalName());
                System.out.println(String.format("add supplier by name: %s", clazz.getCanonicalName()));
            }
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
        }
    }

    boolean isSubClass(Class<?> classA, Class<?> classB) {
        try {
            classA.asSubclass(classB);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private void compileSource(List<File> files) {
        try {
            if (files.isEmpty()) {
                return;
            }

            Map<String, String> sourceContents = new LinkedHashMap<>();
            for (File file : files) {
                String content = FileUtils.readFileToString(file, "UTF-8");
                sourceContents.put(file.getName(), content);
                System.out.println(String.format("compile file: %s", file.getName()));
                log.info("compile file: {}", file.getName());
            }


            Map<String, byte[]> classMap = compiler.compile(sourceContents);
            List<Class<?>> moduleClasses = new ArrayList<>();
            List<Class<?>> functionClasses = new ArrayList<>();
            for (String className : classMap.keySet()) {
                Class<?> clazz = compiler.loadClass(className, classMap);
                log.info("find compiled class: {}", className);
                System.out.println(String.format("find compiled class: %s", className));
                if (isSubClass(clazz, RestFunction.class)) {
                    log.info("find rest function: {}", className);
                    System.out.println(String.format("find rest function: %s", className));
                    functionClasses.add(clazz);
                }
                if (isSubClass(clazz, AbstractModule.class)) {
                    log.info("find module class: {}", className);
                    System.out.println(String.format("find module class: %s", className));
                    moduleClasses.add(clazz);
                }
            }
            registerModule(moduleClasses);
            registerClass(functionClasses);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void scanDir(String dirPath) {
        File dir = new File(dirPath);
        List<File> originFiles = (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        compileSource(sourceFilter(originFiles));
    }

    private List<File> sourceFilter(List<File> originFiles) {
        List<File> files = new ArrayList<>();
        try {
            for (File file : originFiles) {
                if (file.isDirectory()) {
                    continue;
                }
                String fileName = file.getCanonicalPath();
                if (fileName.endsWith(".java") || fileName.endsWith(".groovy")) {
                    files.add(file);
                }
                if (fileName.endsWith(".yaml") || fileName.endsWith(".yml")) {
                    files.add(file);
                }
            }
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
        }
        return files;
    }

    private boolean configFilter(File file) {
        try {
            String fileName = file.getCanonicalPath();
            return fileName.endsWith(".yaml") || fileName.endsWith(".yml");
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
            return false;
        }
    }


    public void watchDirToCompile(String watcherName, String dir) {
        try {
            DirWatcher.DirWatchItem watchItem = new DirWatcher.DirWatchItem(
                    FileSystems.getDefault().getPath(dir),
                    this::sourceFilter,
                    this::compileSource,
                    this::compileSource,
                    new DefaultDirHandler()
            );
            dirWatcher.watchDir(watcherName, watchItem);
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
        }
    }

    private void loadConfig(File configFile) throws Exception {
        String content = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
        RestRouter.RouterConfig config = YamlUtil.fromYaml(content, RestRouter.RouterConfig.class).orElse(null);
        if (config != null) {
            if (this.restRouter == null) {
                this.restRouter = new RestRouter(config);
            } else {
                this.restRouter.update(config);
            }
        }
    }


    public void watchFileToLoad(String watcherName, String fileName) {
        try {
            DirWatcher.FileWatchItem watchItem = new DirWatcher.FileWatchItem(
                    FileSystems.getDefault().getPath(fileName),
                    this::configFilter,
                    this::loadConfig,
                    this::loadConfig,
                    new DefaultFileHandler()
            );
            dirWatcher.watchFile(watcherName, watchItem);
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
        }
    }




    @Override
    public void register(String name, Supplier<RestFunction> supplier) {
        supplierMap.put(name, supplier);
    }

    @Override
    public RestFunction getFunction(String name) {
        Supplier<RestFunction> supplier = supplierMap.get(name);
        if (supplier != null) {
            return supplier.get();
        } else {
            return null;
        }
    }
}

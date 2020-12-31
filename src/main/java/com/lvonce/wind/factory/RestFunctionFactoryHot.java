package com.lvonce.wind.factory;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.internal.asm.$ClassTooLargeException;
import com.lvonce.wind.Pair;
import com.lvonce.wind.RestFunction;
import com.lvonce.wind.compiler.Compiler;
import com.lvonce.wind.compiler.CompilerOfJavaString;
import com.lvonce.wind.watcher.DefaultFileHandler;
import com.lvonce.wind.watcher.DirWatcher;
import com.lvonce.wind.watcher.FileFilter;
import com.lvonce.wind.watcher.FileHandler;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
public class RestFunctionFactoryHot implements RestFunctionFactory {

    private final Map<String, Supplier<RestFunction>> supplierMap = new LinkedHashMap<>();

    private final DirWatcher dirWatcher = new DirWatcher();

    private final CompilerOfJavaString compiler = new CompilerOfJavaString();

    private Injector injector;

    public void registerModule(Iterable<Class<?>> moduleClasses) {
        try {
            List<AbstractModule> modules = new ArrayList<>();
            for (Class<?> moduleClass : moduleClasses) {
                AbstractModule factoryModule = (AbstractModule) moduleClass.newInstance();
                modules.add(factoryModule);
            }
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
            }

            Map<String, byte[]> classMap = compiler.compile(sourceContents);
            List<Class<?>> moduleClasses = new ArrayList<>();
            List<Class<?>> functionClasses = new ArrayList<>();
            for (String className : classMap.keySet()) {
                Class<?> clazz = compiler.loadClass(className, classMap);
                if (isSubClass(clazz, RestFunction.class)) {
                    functionClasses.add(clazz);
                }
                if (isSubClass(clazz, AbstractModule.class)) {
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
                if (!file.isDirectory()) {
                    continue;
                }
                String fileName = file.getCanonicalPath();
                if (fileName.endsWith(".java") || fileName.endsWith(".groovy")) {
                    files.add(file);
                }
            }
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
        }
        return files;
    }


    public void registerWatchingDir(String dir) {
        try {
            DirWatcher.WatchItem watchItem = new DirWatcher.WatchItem(
                    FileSystems.getDefault().getPath(dir),
                    this::sourceFilter,
                    this::compileSource,
                    this::compileSource,
                    new DefaultFileHandler()
            );
            dirWatcher.setDirectoryToWatch("__hot__", watchItem);
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

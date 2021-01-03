package com.lvonce.wind.factory;

import com.lvonce.wind.RestRouter;
import com.lvonce.wind.SQLDataSourceManager;
import com.lvonce.wind.util.YamlUtil;
import com.lvonce.wind.watcher.DefaultFileHandler;
import com.lvonce.wind.watcher.DirSingleFileFilter;
import com.lvonce.wind.watcher.DirWatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@Slf4j
public class ConfigurationLoader {

    private final String restRouterFile;
    private final String sqlDataSourceFile;

    private RestRouter restRouter;

    private SQLDataSourceManager sqlDataSourceManager;

    private final DirWatcher dirWatcher = new DirWatcher();

    @Inject
    public ConfigurationLoader(
            @Named("router-file") String restRouterFile,
            @Named("sql-data-source-file") String sqlDataSourceFile) {
        this.restRouterFile = restRouterFile;
        this.sqlDataSourceFile = sqlDataSourceFile;
        loadRestRouter(FileSystems.getDefault().getPath(this.restRouterFile).toFile());
        loadSqlDataSourceManager(FileSystems.getDefault().getPath(this.sqlDataSourceFile).toFile());
        this.watchRestRouter();
        this.watchDataSource();
    }

    public RestRouter getRestRouter() {
        return restRouter;
    }

    public SQLDataSourceManager getSqlDataSourceManager() {
        return sqlDataSourceManager;
    }


    public void watchRestRouter() {
        try {
            Path path = FileSystems.getDefault().getPath(this.restRouterFile);
            Path parent = path.getParent();
            String originFileName = path.toFile().getCanonicalPath();

            DirSingleFileFilter filter = (File file)-> {
                try {
                    log.info("{} => {}", file.getCanonicalPath(), originFileName);
                    return file.getCanonicalPath().endsWith(originFileName);
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                    return false;
                }
            };

            DirWatcher.FileWatchItem watchItem = new DirWatcher.FileWatchItem(
                    parent,
                    filter,
                    this::loadRestRouter,
                    this::loadRestRouter,
                    new DefaultFileHandler()
            );
            dirWatcher.watchFile("__router__", watchItem);
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
        }
    }

    public void watchDataSource() {
        try {
            Path path = FileSystems.getDefault().getPath(this.sqlDataSourceFile);
            Path parent = path.getParent();
            String originFileName = path.toFile().getCanonicalPath();

            DirSingleFileFilter filter = (File file)-> {
                try {
                    log.info("{} => {}", file.getCanonicalPath(), originFileName);
                    return file.getCanonicalPath().endsWith(originFileName);
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                    return false;
                }
            };

            DirWatcher.FileWatchItem watchItem = new DirWatcher.FileWatchItem(
                    parent,
                    filter,
                    this::loadSqlDataSourceManager,
                    this::loadSqlDataSourceManager,
                    new DefaultFileHandler()
            );
            dirWatcher.watchFile("__sql_source__", watchItem);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }


    private void loadRestRouter(File file) {
        try {
            log.info("loadRestRouter({})", file.getName());
            String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            RestRouter.RouterConfig config = YamlUtil.fromYaml(content, RestRouter.RouterConfig.class).orElse(null);
            if (config != null) {
                if (this.restRouter == null) {
                    this.restRouter = new RestRouter(config);
                } else {
                    this.restRouter.update(config);
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private void loadSqlDataSourceManager(File file) {
        try {
            log.info("loadSqlDataSourceManager({})", file.getName());
            String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            SQLDataSourceManager.SQLDataSourceConfig config = YamlUtil.fromYaml(content, SQLDataSourceManager.SQLDataSourceConfig.class).orElse(null);
            if (config != null) {
                if (this.sqlDataSourceManager == null) {
                    this.sqlDataSourceManager = new SQLDataSourceManager(config);
                } else {
                    this.sqlDataSourceManager.update(config);
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

}

package com.lvonce.wind.watcher;

import com.lvonce.wind.Pair;
import io.methvin.watcher.DirectoryChangeEvent;
import io.methvin.watcher.DirectoryChangeListener;
import io.methvin.watcher.DirectoryWatcher;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DirWatcher {

    @Data
    @AllArgsConstructor
    public static class DirWatchItem {
        private Path path;
        private DirFileFilter fileFilter;
        private DirHandler createHandler;
        private DirHandler updateHandler;
        private DirHandler deleteHandler;
    }

    @Data
    @AllArgsConstructor
    public static class FileWatchItem {
        private Path path;
        private DirSingleFileFilter fileFilter;
        private FileHandler createHandler;
        private FileHandler updateHandler;
        private FileHandler deleteHandler;
    }

    private final Map<String, Pair<DirWatchItem, DirectoryWatcher>> dirWatcherMap = new LinkedHashMap<>();
    private final Map<String, Pair<FileWatchItem, DirectoryWatcher>> fileWatcherMap = new LinkedHashMap<>();

    public void clearDirWatcher(String uri) throws IOException {
        Pair<DirWatchItem, DirectoryWatcher> oldWatcher = this.dirWatcherMap.get(uri);
        if (oldWatcher != null) {
            oldWatcher.getSecond().close();
            this.dirWatcherMap.remove(uri);
        }
    }

    public void clearFileWatcher(String uri) throws IOException {
        Pair<FileWatchItem, DirectoryWatcher> oldWatcher = this.fileWatcherMap.get(uri);
        if (oldWatcher != null) {
            oldWatcher.getSecond().close();
            this.fileWatcherMap.remove(uri);
        }
    }

    private List<File> scanFiles(File file) {
        return (List<File>) FileUtils.listFiles(file, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
    }

    public void watchFile(String uri, FileWatchItem watchItem) throws Exception {
        try {
            clearFileWatcher(uri);
            List<File> originFileList = scanFiles(watchItem.path.toFile());
            File originFile = watchItem.fileFilter.filter(originFileList);
            watchItem.getCreateHandler().handle(originFile);

            DirectoryChangeListener listener = (DirectoryChangeEvent event) -> {
                try {
                    File file = event.path().toFile();
                    log.info("change event: {}", file.getCanonicalPath());
                    if (!watchItem.fileFilter.pass(file)) {
                        return;
                    }

                    log.info("change event passed: {}", file.getCanonicalPath());
                    switch (event.eventType()) {
                        case CREATE:
                            watchItem.getCreateHandler().handle(file);
                            break;
                        case MODIFY:
                            watchItem.getUpdateHandler().handle(file);
                            break;
                        case DELETE:
                            watchItem.getDeleteHandler().handle(file);
                            break;
                    }
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }
            };

            DirectoryWatcher watcher = DirectoryWatcher.builder()
                    .path(watchItem.path) // or use paths(directoriesToWatch)
                    .listener(listener)
                    // .fileHashing(false) // defaults to true
                    // .logger(logger) // defaults to LoggerFactory.getLogger(DirectoryWatcher.class)
                    // .watchService(watchService) // defaults based on OS to either JVM WatchService or the JNA macOS WatchService
                    .build();
            watcher.watchAsync();
            this.fileWatcherMap.put(uri, Pair.of(watchItem, watcher));
        } catch (Exception ex) {
            log.error("watchFile error: {}", ex.getMessage());
        }
    }

    public void watchDir(String uri, DirWatchItem watchItem) throws Exception {
        clearDirWatcher(uri);
        List<File> originFileList = scanFiles(watchItem.path.toFile());
        List<File> fileList = watchItem.fileFilter.filter(originFileList);
        watchItem.getCreateHandler().handle(fileList);

        DirectoryChangeListener listener = (DirectoryChangeEvent event) -> {
            try {
                Path path = watchItem.path;
                File file = path.toFile();
                List<File> originFiles = scanFiles(file);
                List<File> files = watchItem.fileFilter.filter(originFiles);
                System.out.println("change event!");
                switch (event.eventType()) {
                    case CREATE:
                        watchItem.getCreateHandler().handle(files);
                        break;
                    case MODIFY:
                        watchItem.getUpdateHandler().handle(files);
                        break;
                    case DELETE:
                        watchItem.getDeleteHandler().handle(files);
                        break;
                }
            } catch (Exception ex) {
                log.error(ex.getLocalizedMessage());
            }
        };

        DirectoryWatcher watcher = DirectoryWatcher.builder()
                .path(watchItem.path) // or use paths(directoriesToWatch)
                .listener(listener)
                // .fileHashing(false) // defaults to true
                // .logger(logger) // defaults to LoggerFactory.getLogger(DirectoryWatcher.class)
                // .watchService(watchService) // defaults based on OS to either JVM WatchService or the JNA macOS WatchService
                .build();
        watcher.watchAsync();
        this.dirWatcherMap.put(uri, Pair.of(watchItem, watcher));
    }

}

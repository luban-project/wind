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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DirWatcher {

    @Data
    @AllArgsConstructor
    public static class WatchItem {
        private Path path;
        private FileFilter fileFilter;
        private FileHandler createHandler;
        private FileHandler updateHandler;
        private FileHandler deleteHandler;
    }

    private final Map<String, Pair<WatchItem, DirectoryWatcher>> watcherMap = new LinkedHashMap<>();

    public void clearWatcher(String uri) throws IOException {
        Pair<WatchItem, DirectoryWatcher> oldWatcher = this.watcherMap.get(uri);
        if (oldWatcher != null) {
            oldWatcher.getSecond().close();
            this.watcherMap.remove(uri);
        }
    }

    private List<File> scanFiles(File file) {
        return (List<File>) FileUtils.listFiles(file, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
    }

    public void setDirectoryToWatch(String uri, WatchItem watchItem) throws Exception {
        clearWatcher(uri);
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
        this.watcherMap.put(uri, Pair.of(watchItem, watcher));
    }

}

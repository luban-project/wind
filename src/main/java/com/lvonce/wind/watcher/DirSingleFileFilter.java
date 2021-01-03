package com.lvonce.wind.watcher;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@FunctionalInterface
public interface DirSingleFileFilter {
    default File filter(List<File> files) throws Exception {
        for (File file: files) {
            if (pass(file)) {
                return file;
            }
        }
        return null;
    }

    boolean pass(File file);
}

package com.lvonce.wind.watcher;

import java.io.File;
import java.util.List;

@FunctionalInterface
public interface FileFilter {
    List<File> filter(List<File> file) throws Exception;
}

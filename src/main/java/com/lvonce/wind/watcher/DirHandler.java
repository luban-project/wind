package com.lvonce.wind.watcher;

import java.io.File;
import java.util.List;

@FunctionalInterface
public interface DirHandler {
    void handle(List<File> file) throws Exception;
}

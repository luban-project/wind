package com.lvonce.wind.watcher;

import java.io.File;
import java.util.List;

@FunctionalInterface
public interface FileHandler {
    void handle(List<File> file) throws Exception;
}

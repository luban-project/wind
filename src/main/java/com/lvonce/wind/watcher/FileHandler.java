package com.lvonce.wind.watcher;

import java.io.File;

@FunctionalInterface
public interface FileHandler {
    void handle(File file) throws Exception;
}

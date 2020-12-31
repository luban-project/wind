package com.lvonce.wind.compiler;


import java.io.IOException;
import java.util.*;

import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;

public class CompilerOfJavaString {

    JavaCompiler compiler;
    StandardJavaFileManager stdManager;

    public CompilerOfJavaString() {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        this.stdManager = compiler.getStandardFileManager(null, null, null);
    }

    /**
     * Compile a Java source file in memory.
     *
     * @param fileName Java file name, e.g. "Test.java"
     * @param source   The source code as String.
     * @return The compiled results as Map that contains class name as key,
     * class binary as value.
     * @throws IOException If compile error.
     */
    public Map<String, byte[]> compile(String fileName, String source) throws IOException {
        try (MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager)) {
            JavaFileObject javaFileObject = manager.makeStringSource(fileName, source);
            CompilationTask task = compiler.getTask(null, manager, null, null, null, Arrays.asList(javaFileObject));
            Boolean result = task.call();
            if (result == null || !result) {
                throw new RuntimeException("Compilation failed.");
            }
            return manager.getClassBytes();
        }
    }

    public Map<String, byte[]> compile(Map<String, String> sources) throws IOException {
        try (MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager)) {
            List<JavaFileObject> javaFileObjectList = new ArrayList<>();
            for (Map.Entry<String, String> entry : sources.entrySet()) {
                JavaFileObject javaFileObject = manager.makeStringSource(entry.getKey(), entry.getValue());
                javaFileObjectList.add(javaFileObject);
            }
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            CompilationTask task = compiler.getTask(null, manager, diagnostics, null, null, javaFileObjectList);
            Boolean result = task.call();
            if (result == null || !result) {
                for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
                    String compileErrorMsg = String.format("code: %s\n" +
                                    "kind: %s\n" +
                                    "position: %s\n" +
                                    "line: %d\n" +
                                    "start: %s\n" +
                                    "end: %s\n" +
                                    "source: %s\n" +
                                    "message: %s\n",
                            diagnostic.getCode(),
                            diagnostic.getKind(),
                            diagnostic.getPosition(),
                            diagnostic.getLineNumber(),
                            diagnostic.getStartPosition(),
                            diagnostic.getEndPosition(),
                            diagnostic.getSource(),
                            diagnostic.getMessage(Locale.CHINA)
                    );
                    System.out.println(compileErrorMsg);
                }
                throw new RuntimeException("Compilation failed.");
            }
            return manager.getClassBytes();
        }
    }

    /**
     * Load class from compiled classes.
     *
     * @param name       Full class name.
     * @param classBytes Compiled results as a Map.
     * @return The Class instance.
     * @throws ClassNotFoundException If class not found.
     * @throws IOException            If load error.
     */
    public Class<?> loadClass(String name, Map<String, byte[]> classBytes) throws ClassNotFoundException, IOException {
        try (MemoryClassLoader classLoader = new MemoryClassLoader(classBytes)) {
            return classLoader.loadClass(name);
        }
    }
}
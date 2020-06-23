package com.lvonce.wind.util;

import groovy.lang.GroovyClassLoader;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;

@Slf4j
public class GroovyUtil {

    private static final GroovyClassLoader groovyClassLoader;
    static {
        CompilerConfiguration config = new CompilerConfiguration();
        SecureASTCustomizer customizer = buildCustomizer();
        ImportCustomizer importCustomizer = buildImportCustomer();
//        config.addCompilationCustomizers(customizer);
        config.addCompilationCustomizers(importCustomizer);
        groovyClassLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader(), config);
    }

    private static ImportCustomizer buildImportCustomer() {
        ImportCustomizer customizer = new ImportCustomizer();
        customizer.addImport("SqlStatement", "com.lvonce.wind.sql.SqlStatement");

        customizer.addImport("HttpRequestBody", "com.lvonce.wind.http.HttpRequestBody");
        customizer.addImport("HttpRequest", "com.lvonce.wind.http.HttpRequest");
        customizer.addImport("HttpResponse", "com.lvonce.wind.http.HttpResponse");

        customizer.addImport("RestSqlContext", "com.lvonce.wind.RestSqlContext");
        customizer.addImport("RestSqlFunction", "com.lvonce.wind.RestSqlFunction");

        customizer.addStaticStars("java.lang.Math");
        return customizer;
    }

    private static SecureASTCustomizer buildCustomizer() {
        SecureASTCustomizer customizer = new SecureASTCustomizer();
//        customizer.setImportsWhitelist(new ArrayList<>());
        return customizer;
    }

    public static Class<?> parseClass(String content) {
        try {
            return groovyClassLoader.parseClass(content);
        } catch (CompilationFailedException ex) {
            log.error("error: ", ex);
            return null;
        }
    }
}

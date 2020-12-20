package com.lvonce.wind;


import com.lvonce.wind.http.HttpRequest;
import com.lvonce.wind.http.HttpResponse;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.LinkedHashMap;

/**
 * 1. register data source
 * 2. register function factory
 * 3. apply function on data source
 *
 *
 */
public class RestFunctionExecutor {
    private final LinkedHashMap<String, DataSource> sqlDataSource = new LinkedHashMap<>();
    private final RestFunctionFactory factory;

    public RestFunctionExecutor(RestFunctionFactory factory) {
        this.factory = factory;
    }

    public void registerSQLDataSource(String name, DataSource dataSource) {
        this.sqlDataSource.put(name, dataSource);
    }

    public void registerSQLDataSource(String name, String url, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", true);
        HikariDataSource ds = new HikariDataSource(config);
        this.sqlDataSource.put(name, ds);
    }


    public boolean shouldIntercept(String url) {
        return this.factory.getUrls().contains(url);
    }

    public HttpResponse apply(String url, String method, HttpRequest request) {
        RestSqlContext context = new RestSqlContextImpl(this.sqlDataSource, request.getHeaders(), request.getParams(), request.getBody());
        RestFunction func = factory.getFunction(url, method, false);
        func.applyGetWrapper(context);
        return context.getResponse();
    }



}

package com.lvonce.wind;


import com.lvonce.wind.http.HttpRequest;
import com.lvonce.wind.http.HttpResponse;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.LinkedHashMap;

/**
 * 1. register data source
 * 2. register function factory
 * 3. apply function on data source
 *
 *
 */
@Slf4j
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

    public void prepareSQLDataSource(String name, String sql) {
        try {
            DataSource ds = this.sqlDataSource.get(name);
            PreparedStatement createTableStmt = ds.getConnection().prepareStatement(sql);
            createTableStmt.executeUpdate();
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
        }
    }


    public boolean shouldIntercept(String url, String method) {
        return this.factory.hasFunction(url, method);
    }

    public HttpResponse apply(String url, String method, HttpRequest request) throws Exception {
        RestSqlContext context = new RestSqlContextImpl(this.sqlDataSource, request.getHeaders(), request.getParams(), request.getBody());
        RestFunction func = factory.getFunction(url, method, false);
        func.apply(method, context);
        return context.getResponse();
    }



}

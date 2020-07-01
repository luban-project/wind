package com.lvonce.wind;

import com.lvonce.wind.sql.IsolationLevel;
import com.lvonce.wind.sql.TransactionHandler;
import com.lvonce.wind.sql.handler.TransactionHandlerImpl;
import com.lvonce.wind.sql.handler.TransactionHandlerNoneImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import com.lvonce.wind.http.HttpRequest;
import com.lvonce.wind.http.HttpResponse;
import com.lvonce.wind.http.HttpRequestBody;

import java.util.Map;
import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;



@Slf4j
public class RestSqlContextImpl implements RestSqlContext {

    private final Map<String, DataSource> dataSourceMap;

    @Getter
    private final HttpRequest request;

    @Getter
    private final HttpResponse response;

    public RestSqlContextImpl(Map<String, DataSource> dataSourceMap, Map<String, String> headers, Map<String, String[]> params, InputStream inputStream) {
        this.dataSourceMap = dataSourceMap;
        this.request = new HttpRequest(headers, params, new HttpRequestBody(inputStream));
        this.response = new HttpResponse();
    }

    public RestSqlContextImpl(DataSource dataSource, Map<String, String> headers, Map<String, String[]> params, InputStream inputStream) {
        this.dataSourceMap = new LinkedHashMap<>();
        this.dataSourceMap.put("__default__", dataSource);
        this.request = new HttpRequest(headers, params, new HttpRequestBody(inputStream));
        this.response = new HttpResponse();
    }

    @Override
    public TransactionHandler trx() throws SQLException {
        Connection conn = this.dataSourceMap.get("__default__").getConnection();
        return new TransactionHandlerNoneImpl(conn);
    }

    @Override
    public TransactionHandler trx(IsolationLevel isolationLevel) throws SQLException {
        Connection conn = this.dataSourceMap.get("__default__").getConnection();
        if (isolationLevel.equals(IsolationLevel.NONE)) {
            return new TransactionHandlerNoneImpl(conn);
        } else {
            return new TransactionHandlerImpl(conn, isolationLevel);
        }
    }

    @Override
    public TransactionHandler trx(String name, IsolationLevel isolationLevel) throws SQLException {
         Connection conn = this.dataSourceMap.get(name).getConnection();
         if (isolationLevel.equals(IsolationLevel.NONE)) {
             return new TransactionHandlerNoneImpl(conn);
         } else {
             return new TransactionHandlerImpl(conn, isolationLevel);
         }
    }
}

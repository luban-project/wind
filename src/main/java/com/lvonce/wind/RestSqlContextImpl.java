package com.lvonce.wind;

import com.lvonce.wind.http.HttpRequest;
import com.lvonce.wind.http.HttpRequestBody;
import com.lvonce.wind.http.HttpResponse;
import com.lvonce.wind.sql.NamedParameterStatement;
import com.lvonce.wind.sql.SqlStatement;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;


@Slf4j
public class RestSqlContextImpl implements RestSqlContext {

    private final DataSource dataSource;

    @Getter
    private final HttpRequest request;

    @Getter
    private final HttpResponse response;

    public RestSqlContextImpl(DataSource dataSource, Map<String, String> headers, Map<String, String[]> params, InputStream inputStream) {
        this.dataSource = dataSource;
        this.request = new HttpRequest(headers, params, new HttpRequestBody(inputStream));
        this.response = new HttpResponse();
    }

    public SqlStatement sql(String sql) {
        try {
            Connection conn = dataSource.getConnection();
            NamedParameterStatement namedParameterStatement = new NamedParameterStatement(conn, sql);
            return new SqlStatement(namedParameterStatement);
        } catch (Exception ex) {
            log.info("SqlStatement prepare({}) error: {}", sql, ex);
            return null;
        }
    }

}

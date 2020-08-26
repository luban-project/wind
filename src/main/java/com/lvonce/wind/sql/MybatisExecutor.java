package com.lvonce.wind.sql;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.*;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.managed.ManagedTransaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.*;

public class MybatisExecutor {
//    SqlSessionFactory factory;
    Executor executor;
    Configuration config;
    XMLLanguageDriver languageDriver;

    public static MybatisExecutor build(Connection connection) {
        MybatisExecutor executor = new MybatisExecutor();
        Configuration config = new Configuration();
        Transaction trx = new ManagedTransaction(connection, false);

        executor.config = config;
        executor.languageDriver = new XMLLanguageDriver();
        executor.executor = config.newExecutor(trx);
        return executor;
    }

    public List<Map<String, Object>> query(String xmlSql, Map<String, Object> queryParams) throws Exception {
        String id = UUID.nameUUIDFromBytes(xmlSql.getBytes()).toString();
        String xml = "<script>" + xmlSql + "</script>";
        SqlSource sqlSource = this.languageDriver.createSqlSource(config, xml, null);
        MappedStatement.Builder builder = new MappedStatement.Builder(
                config, id, sqlSource, SqlCommandType.SELECT
        );

        // TODO: cache the statement
        ResultMap resultMap = new ResultMap.Builder(config, id, Map.class, new ArrayList<>()).build();
        builder.resultMaps(Collections.singletonList(resultMap));
        MappedStatement statement = builder.build();

        List<Map<String, Object>> results = executor.query(statement, queryParams, RowBounds.DEFAULT, null);
        return results;
    }

    private List<Map<String, Object>> query(String id, String xmlSql, Map<String, Object> queryParams) throws Exception {
        String xml = "<script>" + xmlSql + "</script>";
        SqlSource sqlSource = this.languageDriver.createSqlSource(config, xml, null);
        MappedStatement.Builder builder = new MappedStatement.Builder(
                config, id, sqlSource, SqlCommandType.SELECT
        );

        ResultMap resultMap = new ResultMap.Builder(config, id, Map.class, new ArrayList<>()).build();
        builder.resultMaps(Collections.singletonList(resultMap));
        MappedStatement statement = builder.build();

        List<Map<String, Object>> results = executor.query(statement, queryParams, RowBounds.DEFAULT, null);
        return results;
    }


}

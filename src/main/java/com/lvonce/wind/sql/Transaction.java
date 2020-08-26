package com.lvonce.wind.sql;

import com.lvonce.wind.sql.handler.TransactionHandlerImpl;
import com.lvonce.wind.sql.handler.TransactionHandlerNoneImpl;
import com.lvonce.wind.sql.statment.NamedParameterStatement;
import com.lvonce.wind.sql.statment.SqlStatement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

@Slf4j
public class Transaction {
    private final Connection connection;
    private final IsolationLevel level;
    private final TransactionState state;
    private final MybatisExecutor executor;

    public Transaction(Connection connection, IsolationLevel level, TransactionState state) {
        this.connection = connection;
        this.level = level;
        this.state = state;
        this.executor = MybatisExecutor.build(connection);
    }


    public TransactionHandler trx() {
        if (level.equals(IsolationLevel.NONE)) {
            return new TransactionHandlerNoneImpl(connection, state);
        } else {
            return new TransactionHandlerImpl(connection, level, state);
        }
    }

    public List<Map<String, Object>> mybatisSql(String xmlSql, Map<String, Object> queryParams) {
        try {
            return executor.query(xmlSql, queryParams);
        } catch (Exception ex) {
            log.info("SqlStatement prepare({}) error: {}", xmlSql, ex);
            return null;
        }
    }

    public List<Map<String, Object>> namedSql(String namedSql, Map<String, Object> queryParams) {
        try {
            NamedParameterStatement namedParameterStatement = new NamedParameterStatement(connection, namedSql);
            state.registerStatement(namedParameterStatement);
            SqlStatement sqlStatement = new SqlStatement(state, namedParameterStatement);
            return sqlStatement.query(queryParams);
        } catch (Exception ex) {
            log.info("SqlStatement prepare({}) error: {}", namedSql, ex);
            return null;
        }
    }


    public SqlStatement sql(String sql) {
        try {
            NamedParameterStatement namedParameterStatement = new NamedParameterStatement(connection, sql);
            state.registerStatement(namedParameterStatement);
            return new SqlStatement(state, namedParameterStatement);
        } catch (Exception ex) {
            log.info("SqlStatement prepare({}) error: {}", sql, ex);
            return null;
        }
    }
}

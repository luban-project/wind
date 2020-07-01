package com.lvonce.wind.sql;

import com.lvonce.wind.sql.handler.TransactionHandlerImpl;
import com.lvonce.wind.sql.handler.TransactionHandlerNoneImpl;
import com.lvonce.wind.sql.statment.NamedParameterStatement;
import com.lvonce.wind.sql.statment.SqlStatement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;

@Slf4j
@AllArgsConstructor
public class Transaction {
    private final Connection connection;
    private final IsolationLevel level;
    private final TransactionState state;

    public TransactionHandler trx() {
        if (level.equals(IsolationLevel.NONE)) {
            return new TransactionHandlerNoneImpl(connection, state);
        } else {
            return new TransactionHandlerImpl(connection, level, state);
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

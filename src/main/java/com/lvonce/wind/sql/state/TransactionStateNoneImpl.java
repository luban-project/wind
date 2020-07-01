package com.lvonce.wind.sql.state;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import com.lvonce.wind.sql.TransactionState;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionStateNoneImpl implements TransactionState {

    private final Connection connection;

    private final List<Statement> statements = new ArrayList<>();

    private final List<ResultSet> resultSets = new ArrayList<>();

    public TransactionStateNoneImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void registerStatement(Statement statement) {
        statements.add(0, statement);
    }

    @Override
    public void registerResult(ResultSet resultSet) {
        resultSets.add(0, resultSet);
    }

    @Override
    public void commit() throws SQLException {
    }

    @Override
    public void rollback() {
    }

    @Override
    public void rollback(Savepoint savepoint) {
    }

    @Override
    public void close() {
        try {
            if (connection != null && connection.isClosed()) {
                log.debug("CLOSING_CLOSED_CONNECTION, Tried to close already closed connection! " +
                        "Check for some unwanted close() in your code.");
                return;
            }

            for (ResultSet resultSet : resultSets) {
                resultSet.close();
            }
            for (Statement statement : statements) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            log.debug("TRANSACTION_CLOSE_ERROR, Failed to close transaction.", ex);
        }
    }
}

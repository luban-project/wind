package com.lvonce.wind.sql.handler;

import java.sql.Connection;
import java.sql.SQLException;

import com.lvonce.wind.sql.*;
import com.lvonce.wind.sql.state.TransactionStateNoneImpl;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class TransactionHandlerNoneImpl implements TransactionHandler {

    private final Connection connection;

    private final TransactionState transactionState;

    public TransactionHandlerNoneImpl(Connection connection) {
        this.connection = connection;
        this.transactionState = new TransactionStateNoneImpl(connection);
    }

    public TransactionHandlerNoneImpl(Connection connection, TransactionState state) {
        this.connection = connection;
        this.transactionState = state;
    }

    @Override
    public TransactionResult apply(TransactionFunc func) throws SQLException {
        Transaction transaction = new Transaction(connection, IsolationLevel.NONE, transactionState);
        return func.execute(transaction);
    }
}

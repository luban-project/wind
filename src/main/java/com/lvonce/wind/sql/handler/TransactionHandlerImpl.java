package com.lvonce.wind.sql.handler;

import com.lvonce.wind.sql.*;
import com.lvonce.wind.sql.state.TransactionStateImpl;
import lombok.extern.slf4j.Slf4j;


import java.sql.Connection;
import java.sql.Savepoint;
import java.util.UUID;

@Slf4j
public class TransactionHandlerImpl implements TransactionHandler {

    private final Connection connection;

    private final IsolationLevel isolationLevel;

    private final TransactionState transactionState;

    private final boolean isNestedTransaction;
    private Savepoint savepoint;

    public TransactionHandlerImpl(Connection connection, IsolationLevel isolationLevel) {
        this.connection = connection;
        this.isNestedTransaction = false;
        this.transactionState = new TransactionStateImpl(connection);
        this.savepoint = null;
        this.isolationLevel = isolationLevel;
    }

    public TransactionHandlerImpl(Connection connection, IsolationLevel isolationLevel, TransactionState state) {
        this.connection = connection;
        this.isNestedTransaction = true;
        this.transactionState = state;
        this.savepoint = null;
        this.isolationLevel = isolationLevel;
    }

    @Override
    public TransactionResult apply(TransactionFunc func) {
        try {
            if (!isNestedTransaction) {
                connection.setTransactionIsolation(isolationLevel.getLevel());
                connection.setAutoCommit(false);
            } else {
                String savePointName = UUID.randomUUID().toString();
                this.savepoint = connection.setSavepoint(savePointName);
            }

            TransactionResult result = func.execute(new Transaction(connection, isolationLevel, transactionState));
            transactionState.commit();
            return result;
        } catch (Exception ex) {
            if (this.savepoint == null) {
                transactionState.rollback();
            } else {
                transactionState.rollback(savepoint);
            }
            return new TransactionResult(false, 0, ex.getLocalizedMessage(), null);
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    if (!isNestedTransaction) {
                        connection.setAutoCommit(true);
                    }
                }
            } catch (Exception ex) {
                log.debug("Transaction setAutoCommit(true) error:", ex);
            }
            transactionState.close();
        }
    }
}

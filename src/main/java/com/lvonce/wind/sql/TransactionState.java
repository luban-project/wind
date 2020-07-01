package com.lvonce.wind.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

public interface TransactionState {
    void registerStatement(Statement statement);
    void registerResult(ResultSet resultSet);
    void commit() throws SQLException;
    void rollback();
    void rollback(Savepoint savepoint);
    void close();
}

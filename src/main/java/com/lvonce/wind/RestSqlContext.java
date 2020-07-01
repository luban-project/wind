package com.lvonce.wind;

import java.sql.SQLException;
import com.lvonce.wind.sql.IsolationLevel;
import com.lvonce.wind.sql.TransactionHandler;

public interface RestSqlContext extends RestContext {
    TransactionHandler trx() throws SQLException;
    TransactionHandler trx(IsolationLevel isolationLevel) throws SQLException;
    TransactionHandler trx(String name, IsolationLevel level) throws SQLException;
}

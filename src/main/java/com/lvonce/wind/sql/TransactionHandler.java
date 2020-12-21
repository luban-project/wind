package com.lvonce.wind.sql;

import java.sql.SQLException;

public interface TransactionHandler {
    TransactionResult apply(TransactionFunc func) throws SQLException;
}

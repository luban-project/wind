package com.lvonce.wind.sql;

import java.sql.SQLException;

@FunctionalInterface
public interface TransactionFunc {
    TransactionResult execute(Transaction trx) throws SQLException;
}

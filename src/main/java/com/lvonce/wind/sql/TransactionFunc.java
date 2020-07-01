package com.lvonce.wind.sql;

@FunctionalInterface
public interface TransactionFunc {
    TransactionResult execute(Transaction trx);
}

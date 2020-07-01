package com.lvonce.wind.sql;

public interface TransactionHandler {
    TransactionResult apply(TransactionFunc func);
}

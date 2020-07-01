package com.lvonce.wind.sql;

import java.sql.Connection;

public enum IsolationLevel {
    NONE(Connection.TRANSACTION_NONE),
    READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),
    READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
    REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
    SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

    private final int level;

    IsolationLevel(int value) {
        this.level = value;
    }

    public int getLevel() {
        return this.level;
    }
}

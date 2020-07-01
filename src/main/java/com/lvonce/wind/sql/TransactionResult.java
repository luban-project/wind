package com.lvonce.wind.sql;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResult {
    boolean success;
    int count;
    String errMsg;
    Object body;

    public static TransactionResult of(boolean success) {
        return new TransactionResult(success, 0, "", null);
    }

    public static TransactionResult of(boolean success, int count) {
        return new TransactionResult(success, count, "", null);
    }

    public static TransactionResult of(boolean success, String errMsg) {
        return new TransactionResult(success, 0, errMsg, null);
    }

    public static TransactionResult of(boolean success, int count, Object body) {
        return new TransactionResult(success, count, "", body);
    }
}

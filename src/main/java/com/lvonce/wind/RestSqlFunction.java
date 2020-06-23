package com.lvonce.wind;

public interface RestSqlFunction extends RestFunction {

    default void applyGet(RestContext ctx) {
        applyGet((RestSqlContext) ctx);
    }

    default void applyPut(RestContext ctx) {
        applyPut((RestSqlContext) ctx);
    }

    default void applyDelete(RestContext ctx) {
        applyDelete((RestSqlContext) ctx);
    }

    default void applyPost(RestContext ctx) {
        applyPost((RestSqlContext) ctx);
    }

    default void applyGet(RestSqlContext ctx) { }

    default void applyPut(RestSqlContext ctx) { }

    default void applyDelete(RestSqlContext ctx) { }

    default void applyPost(RestSqlContext ctx) { }
}

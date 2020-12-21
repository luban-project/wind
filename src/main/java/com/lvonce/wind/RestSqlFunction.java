package com.lvonce.wind;

public interface RestSqlFunction extends RestFunction {

    @Override
    default void applyGet(RestContext ctx) throws Exception {
        applyGet((RestSqlContext) ctx);
    }

    @Override
    default void applyPut(RestContext ctx) throws Exception {
        applyPut((RestSqlContext) ctx);
    }

    @Override
    default void applyDelete(RestContext ctx) throws Exception {
        applyDelete((RestSqlContext) ctx);
    }

    @Override
    default void applyPost(RestContext ctx) throws Exception {
        applyPost((RestSqlContext) ctx);
    }

    default void applyGet(RestSqlContext ctx) throws Exception {
    }

    default void applyPut(RestSqlContext ctx) throws Exception {
    }

    default void applyDelete(RestSqlContext ctx) throws Exception {
    }

    default void applyPost(RestSqlContext ctx) throws Exception {
    }
}

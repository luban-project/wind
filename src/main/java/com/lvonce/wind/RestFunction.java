package com.lvonce.wind;

public interface RestFunction {

    default void applyGet(RestContext body) { }

    default void applyPut(RestContext body) { }

    default void applyDelete(RestContext body) { }

    default void applyPost(RestContext body) { }
}

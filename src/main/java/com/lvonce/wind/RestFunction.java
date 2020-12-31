package com.lvonce.wind;

import com.lvonce.wind.http.HttpResponse;
import org.apache.commons.lang3.NotImplementedException;
import org.codehaus.groovy.runtime.powerassert.PowerAssertionError;

import java.util.Map;

public interface RestFunction {

    default void apply(String method, RestContext ctx) throws Exception {
        switch (method) {
            case "GET":
                applyGet(ctx);
                break;
            case "POST":
                applyPost(ctx);
                break;
            case "PUT":
                applyPut(ctx);
                break;
            case "DELETE":
                applyDelete(ctx);
                break;
            default:
                throw new NotImplementedException(method);
        }
    }

    default void applyGet(RestContext body) throws Exception { }

    default void applyPut(RestContext body) throws Exception { }

    default void applyDelete(RestContext body) throws Exception { }

    default void applyPost(RestContext body) throws Exception { }
}

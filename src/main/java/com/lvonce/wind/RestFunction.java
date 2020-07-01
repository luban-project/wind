package com.lvonce.wind;

import com.lvonce.wind.http.HttpResponse;
import org.codehaus.groovy.runtime.powerassert.PowerAssertionError;

public interface RestFunction {

    default void applyGet(RestContext body) { }

    default void applyPut(RestContext body) { }

    default void applyDelete(RestContext body) { }

    default void applyPost(RestContext body) { }

    default void applyGetWrapper(RestContext body) {
        try {
            applyGet(body);
        } catch (PowerAssertionError error) {
            HttpResponse response = body.getResponse();
            response.setBody(null);
            response.setErrCode("PARAM_ASSERT_ERROR");
            response.setErrMessage(error.getLocalizedMessage());
        }
    }

    default void applyPutWrapper(RestContext body) {
        try {
            applyPut(body);
        } catch (PowerAssertionError error) {
            HttpResponse response = body.getResponse();
            response.setBody(null);
            response.setErrCode("PARAM_ASSERT_ERROR");
            response.setErrMessage(error.getLocalizedMessage());
        }
    }

    default void applyDeleteWrapper(RestContext body) {
        try {
            applyDelete(body);
        } catch (PowerAssertionError error) {
            HttpResponse response = body.getResponse();
            response.setBody(null);
            response.setErrCode("PARAM_ASSERT_ERROR");
            response.setErrMessage(error.getLocalizedMessage());
        }
    }

    default void applyPostWrapper(RestContext body) {
        try {
            applyPost(body);
        } catch (PowerAssertionError error) {
            HttpResponse response = body.getResponse();
            response.setBody(null);
            response.setErrCode("PARAM_ASSERT_ERROR");
            response.setErrMessage(error.getLocalizedMessage());
        }
    }
}

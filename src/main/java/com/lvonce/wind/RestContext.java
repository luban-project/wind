package com.lvonce.wind;



import com.lvonce.wind.http.HttpRequest;
import com.lvonce.wind.http.HttpRequestBody;
import com.lvonce.wind.http.HttpResponse;
import com.lvonce.wind.util.JsonUtil;

import java.util.LinkedHashMap;


public interface RestContext {
    HttpRequest request = new HttpRequest(new LinkedHashMap<>(), new LinkedHashMap<>(), new HttpRequestBody(null));;
    HttpResponse response = new HttpResponse();

    default HttpRequest getRequest() {
        return request;
    }

    default HttpResponse getResponse() {
        return response;
    }

    default <T> String json(T obj) {
        return JsonUtil.toJson(obj, "");
    }
}

package com.lvonce.wind.http;

public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    NONE;

    public static HttpMethod from(String name) {
        switch (name) {
            case "GET": return GET;
            case "POST": return POST;
            case "PUT": return PUT;
            case "DELETE": return DELETE;
            default: return NONE;
        }
    }
}

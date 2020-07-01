package com.lvonce.wind.http;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class HttpResponse {

    private Map<String, String> cookies = new LinkedHashMap<>();

    private Map<String, String> headers = new LinkedHashMap<>();

    private Object body;

    private String errCode;

    private String errMessage;
}

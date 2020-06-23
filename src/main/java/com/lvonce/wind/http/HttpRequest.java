package com.lvonce.wind.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Map;

@Data
@AllArgsConstructor
public class HttpRequest {

    @Getter
    private Map<String, String> headers;

    @Getter
    private Map<String, String[]> params;

    @Getter
    private HttpRequestBody body;
}

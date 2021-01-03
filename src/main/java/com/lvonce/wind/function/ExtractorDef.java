package com.lvonce.wind.function;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ExtractorDef {
    public static enum ExtractSource {
        HEADER,
        PATH,
        PARAM,
        BODY,
        COOKIE
    }

    @Data
    @AllArgsConstructor
    public static class ExtractItem {
        int idx;
        ExtractSource source;
        String name;
        Class<?> valueType;
    }

    List<ExtractItem> extractItems = new ArrayList<>();
    Map<String, ExtractItem> headerExtractItems = new LinkedHashMap<>();
    Map<String, ExtractItem> pathExtractItems = new LinkedHashMap<>();
    Map<String, ExtractItem> paramExtractItems = new LinkedHashMap<>();
    Map<String, ExtractItem> bodyExtractItems = new LinkedHashMap<>();
    Map<String, ExtractItem> cookieExtractItems = new LinkedHashMap<>();

    public void addExtractItem(ExtractSource source, String name, Class<?> clazz) {
        int idx = extractItems.size();
        ExtractItem item = new ExtractItem(idx, source, name, clazz);
        extractItems.add(item);
        switch (source) {
            case BODY: bodyExtractItems.put(item.name, item);break;
            case PATH: pathExtractItems.put(item.name, item); break;
            case PARAM: paramExtractItems.put(item.name, item); break;
            case HEADER: headerExtractItems.put(item.name, item); break;
            case COOKIE: cookieExtractItems.put(item.name, item); break;
        }
    }
}

package com.lvonce.wind.function;

import com.lvonce.wind.http.HttpRequestBody;
import com.lvonce.wind.util.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Extractor {

    public static boolean isSubClass(Class<?> classA, Class<?> classB) {
        try {
            classA.asSubclass(classB);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static Object convert(String value, Class<?> clazz) {
        return JsonUtil.fromJson(value, clazz).orElse(null);
    }

    public static Object convert(String[] value, Class<?> clazz) {
        if (isSubClass(clazz, String.class)) {
            return String.join(",", value);
        }
        String content = JsonUtil.toJson(value).orElse("");
        return JsonUtil.fromJson(content, clazz).orElse(null);
    }


    public static ExtractorResult extract(HttpServletRequest httpServletRequest, ExtractorDef extractorDef) throws IOException {
        ExtractorResult result = ExtractorResult.make(extractorDef);
        Map<String, ExtractorDef.ExtractItem> headerExtractMap = extractorDef.headerExtractItems;
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            ExtractorDef.ExtractItem extractItem = headerExtractMap.get(headerName);
            if (extractItem != null) {
                String header = httpServletRequest.getHeader(headerName);
                Object value = convert(header, extractItem.valueType);
                result.setValue(extractItem.idx, value);
            }
        }

        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        Map<String, ExtractorDef.ExtractItem> paramExtractMap = extractorDef.paramExtractItems;
        for (Map.Entry<String, ExtractorDef.ExtractItem> entry: paramExtractMap.entrySet()) {
            ExtractorDef.ExtractItem item = entry.getValue();
            String[] content = parameterMap.get(item.name);
            Object value = convert(content, item.valueType);
            result.setValue(item.idx, value);
        }

        Map<String, Object> bodyMap = JsonUtil.readJsonToMap(httpServletRequest.getInputStream());
        Map<String, ExtractorDef.ExtractItem> bodyExtractMap = extractorDef.bodyExtractItems;
        for (Map.Entry<String, ExtractorDef.ExtractItem> entry: bodyExtractMap.entrySet()) {
            ExtractorDef.ExtractItem item = entry.getValue();
            Object value = bodyMap.get(item.name);
            result.setValue(item.idx, value);
        }
        return result;
    }
}

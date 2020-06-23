package com.lvonce.wind.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class JsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();


    public static Optional<JsonNode> readJsonNode(String json) {
        try {
            return Optional.of(mapper.readTree(json));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public static Optional<JsonNode> readJsonNode(InputStream json) {
        try {
            return Optional.of(mapper.readTree(json));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public static JsonNode readJsonNode(InputStream json, JsonNode defaultNode) {
        try {
            return mapper.readTree(json);
        } catch (Exception ex) {
            return defaultNode;
        }
    }

    private static final TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
    public static Map<String, Object> readJsonToMap(InputStream json) {
        try {
            return mapper.readValue(json, typeRef);
        } catch (Exception ex) {
            return new LinkedHashMap<>();
        }
    }

    public static <T> Optional<String> toJson(T obj) {
        try {
            return Optional.of(mapper.writeValueAsString(obj));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public static <T> String toJson(T obj, String defaultJson) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception ex) {
            return defaultJson;
        }
    }




}

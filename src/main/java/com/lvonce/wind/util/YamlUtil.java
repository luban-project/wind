package com.lvonce.wind.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;


import java.util.Optional;

public class YamlUtil {

    private static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    {
        mapper.findAndRegisterModules();
    }


    public static <T> Optional<T> fromYaml(String content, Class<T> tClass) {
        try {
            return Optional.of(mapper.readValue(content, tClass));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public static <T> Optional<String> toYaml(T obj) {
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

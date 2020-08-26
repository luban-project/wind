package com.lvonce.wind.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class LocalDateTimeConverterMap {
    static Map<String, DateTimeFormatter> dateFormatterMap = new LinkedHashMap<>();
    static {
        dateFormatterMap.put("yyyy-MM-dd HH:mm:ss", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static Converter getToStrConverter(String pattern) {
        return (Object obj) -> {
            LocalDateTime dateTime = (LocalDateTime)obj;
            DateTimeFormatter formatter = dateFormatterMap.getOrDefault(pattern, DateTimeFormatter.ofPattern(pattern));
            return dateTime.format(formatter);
        };
    }

    public static Converter getFromStrConverter(String pattern) {
        return (Object obj) -> {
            String dateTime = (String) obj;
            DateTimeFormatter formatter = dateFormatterMap.getOrDefault(pattern, DateTimeFormatter.ofPattern(pattern));
            return LocalDateTime.parse(dateTime, formatter);
        };
    }
}

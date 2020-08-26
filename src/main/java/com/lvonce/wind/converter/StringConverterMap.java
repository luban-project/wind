package com.lvonce.wind.converter;

import com.lvonce.wind.Pair;

import java.math.BigDecimal;
import java.util.Map;

public class StringConverterMap {
    static Map<Pair<String, String>, Converter> converterMap;

    static {
        converterMap.put(Pair.of("String", "Byte"), (Object obj)-> Byte.parseByte((String)obj));
        converterMap.put(Pair.of("String", "Short"), (Object obj)-> Short.parseShort((String)obj));
        converterMap.put(Pair.of("String", "Int"), (Object obj)-> Integer.parseInt((String)obj));
        converterMap.put(Pair.of("String", "Long"), (Object obj)-> Long.parseLong((String)obj));

        converterMap.put(Pair.of("String", "Float"), (Object obj)-> Float.parseFloat((String)obj));
        converterMap.put(Pair.of("String", "Double"), (Object obj)-> Double.parseDouble((String)obj));

        converterMap.put(Pair.of("String", "BigDecimal"), (Object obj)-> new BigDecimal((String)obj));
        converterMap.put(Pair.of("String", "Boolean"), (Object obj)-> Boolean.parseBoolean((String)obj));
    }


    public static Converter getFromStrConverter(String source, String target) {
        return converterMap.get(Pair.of(source, target));
    }

    public static Converter getToStrConverter() {
        return Object::toString;
    }

}

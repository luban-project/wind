package com.lvonce.wind.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lvonce.wind.util.JsonUtil;
import lombok.AllArgsConstructor;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Map;

@AllArgsConstructor
public class HttpRequestBody {
    private static ObjectMapper mapper = new ObjectMapper();
    private InputStream input;


    public Map<String, Object> asMap() {
        return JsonUtil.readJsonToMap(input);
    }

    public String asText() {
        try {
            return mapper.readValue(input, String.class);
        } catch (Exception ex) {
            return "";
        }
    }

    public Short asShort() {
        try {
            return mapper.readValue(input, Short.class);
        } catch (Exception ex) {
            return 0;
        }
    }

    public Integer asInt() {
        try {
            return mapper.readValue(input, Integer.class);
        } catch (Exception ex) {
            return 0;
        }
    }


    public Long asLong() {
        try {
            return mapper.readValue(input, Long.class);
        } catch (Exception ex) {
            return 0L;
        }
    }


    public Float asFloat() {
        try {
            return mapper.readValue(input, Float.class);
        } catch (Exception ex) {
            return 0.0f;
        }
    }

    public Double asDouble() {
        try {
            return mapper.readValue(input, Double.class);
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public BigDecimal asBigDecimal() {
        try {
            return mapper.readValue(input, BigDecimal.class);
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }
}

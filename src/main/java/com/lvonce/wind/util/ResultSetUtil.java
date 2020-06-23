package com.lvonce.wind.util;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResultSetUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    public static JsonNode toJson(ResultSet resultSet) throws SQLException {
        return toJson(resultSet, false);
    }

    public static List<Map<String, Object>> toArrayOfMap(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (resultSet == null) {
            return resultList;
        }
        ResultSetMetaData meta = resultSet.getMetaData();
        int colCount = meta.getColumnCount();
        while (resultSet.next()) {
            Map<String, Object> columnObject = new LinkedHashMap<>();
            for (int i = 1; i <= colCount; ++i) {
                String column = meta.getColumnName(i);
                String type = meta.getColumnClassName(i);
                Object value = resultSet.getObject(column);
                columnObject.put(getColumnName(column), value);
            }
            resultList.add(columnObject);
        }
        return resultList;
    }



    public static JsonNode toJson(ResultSet resultSet, boolean normalizeColumnNames) throws SQLException {

        if (resultSet == null) {
            return JsonNodeFactory.instance.nullNode();
        }

        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
        ResultSetMetaData meta = resultSet.getMetaData();
        int colCount = meta.getColumnCount();

        while (resultSet.next()) {
            ObjectNode objectResult = JsonNodeFactory.instance.objectNode();
            for (int i = 1; i <= colCount; ++i) {
                String column = meta.getColumnName(i);
                String type = meta.getColumnClassName(i);
                Object value = resultSet.getObject(column);

                if (value != null) {
                    if (type.equals(String.class.getTypeName())) {
                        objectResult.set(getColumnName(column), JsonNodeFactory.instance.textNode((String)value) );
                    } else if (type.equals(Integer.class.getTypeName())) {
                        objectResult.set(getColumnName(column), JsonNodeFactory.instance.numberNode((Integer) value));
                    } else if (type.equals(BigDecimal.class.getTypeName())) {
                        objectResult.set(getColumnName(column), JsonNodeFactory.instance.numberNode((BigDecimal) value));
                    } else if (type.equals(Long.class.getTypeName())) {
                        objectResult.set(getColumnName(column), JsonNodeFactory.instance.numberNode((Long) value));
                    } else if (type.equals(Float.class.getTypeName())) {
                        objectResult.set(getColumnName(column), JsonNodeFactory.instance.numberNode((Float) value));
                    } else if (type.equals(Double.class.getTypeName())) {
                        objectResult.set(getColumnName(column), JsonNodeFactory.instance.numberNode((Double) value));
                    } else if (type.equals(Boolean.class.getTypeName())) {
                        objectResult.set(getColumnName(column), JsonNodeFactory.instance.booleanNode((Boolean) value));
                    } else if (type.equals(Short.class.getTypeName())) {
                        objectResult.set(getColumnName(column), JsonNodeFactory.instance.numberNode((Short) value));
                    } else {
                        objectResult.set(getColumnName(column), JsonNodeFactory.instance.textNode((String.valueOf(value))));
                    }
                } else {
                    objectResult.set(column, JsonNodeFactory.instance.nullNode());
                }
            }
            arrayNode.add(objectResult);
        }
        if (arrayNode.size() == 1) {
            return arrayNode.get(0);
        }
        return arrayNode;
    }

    /**
     * This will return the column name or a normalized version of the column name.
     *
     * @param column
     * @return column name
     */
    private static String getColumnName(String column) {
        return column;
    }
}


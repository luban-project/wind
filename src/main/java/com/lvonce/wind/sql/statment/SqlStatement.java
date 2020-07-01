package com.lvonce.wind.sql.statment;

import com.lvonce.wind.sql.TransactionState;
import com.lvonce.wind.util.ResultSetUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class SqlStatement {

    private TransactionState state;

    private NamedParameterStatement namedStatement;

    private boolean bindValue(Map<String, Object> values) {
        try {
            Map<String, List<Integer>> nameMap = namedStatement.getNameIndexMap();
            for (String paramName : nameMap.keySet()) {
                namedStatement.setString(paramName, values.get(paramName).toString());
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    private boolean bindValue(String name, Object value) {
        try {
            Map<String, List<Integer>> nameMap = namedStatement.getNameIndexMap();
            if (!nameMap.containsKey(name)) {
                return false;
            }
            namedStatement.setString(name, value.toString());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public List<Map<String, Object>> query(Map<String, Object> values) throws Exception {
        boolean bindSuccess = bindValue(values);
        if (!bindSuccess) {
            return null;
        }
        ResultSet result = namedStatement.executeQuery();
        state.registerResult(result);
        return ResultSetUtil.toArrayOfMap(result);
    }

    public int update(Map<String, Object> values) throws Exception {
        boolean bindSuccess = bindValue(values);
        if (!bindSuccess) {
            return 0;
        }
        return namedStatement.executeUpdate();
    }

    public List<Map<String, Object>> query(String name, Object value) throws Exception {
        boolean bindSuccess = bindValue(name, value);
        if (!bindSuccess) {
            return null;
        }
        ResultSet result = namedStatement.executeQuery();
        state.registerResult(result);
        return ResultSetUtil.toArrayOfMap(result);
    }

    public int update(String name, Object value) throws Exception {
        boolean bindSuccess = bindValue(name, value);
        if (!bindSuccess) {
            return 0;
        }
        return namedStatement.executeUpdate();
    }

    public void close() throws Exception {
        namedStatement.close();
    }
}

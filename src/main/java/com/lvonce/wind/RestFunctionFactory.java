package com.lvonce.wind;

import com.lvonce.wind.util.GroovyUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
public class RestFunctionFactory {

    private final Map<Pair<String, String>, Supplier<RestFunction>> supplierMap = new LinkedHashMap<>();

    private final Map<Pair<String, String>, RestFunction> functionCache = new LinkedHashMap<>();

    private static final RestFunctionFactory instance = new RestFunctionFactory();

    public static RestFunctionFactory getInstance() {
        return instance;
    }

    private RestFunctionFactory() {}

    public boolean register(String uri, String method, String lang, String script) {
        if (lang.equalsIgnoreCase("groovy")) {
            Class<?> clazz = GroovyUtil.parseClass(script);
            if (clazz == null) {
                return false;
            }
            Supplier<RestFunction> supplier = () -> {
                try {
                    return (RestFunction) clazz.newInstance();
                } catch (Exception ex) {
                    return null;
                }
            };
            supplierMap.put(Pair.of(uri, method), supplier);
            return true;
        }
        return false;
    }

    public boolean register(String uri, String method, Supplier<RestFunction> supplier) {
        supplierMap.put(Pair.of(uri, method), supplier);
        return true;
    }

    public RestFunction getFunctionCache(String uri, String method) {
        Pair<String, String> key = Pair.of(uri, method);
        if (functionCache.containsKey(key)) {
            return functionCache.get(key);
        } else {
            Supplier<RestFunction> supplier = supplierMap.get(Pair.of(uri, method));
            RestFunction func = supplier.get();
            functionCache.put(key, func);
            return func;
        }
    }

    public RestFunction getFunction(String uri, String method, boolean isHot) {
        if (isHot) {
            Supplier<RestFunction> supplier = supplierMap.get(Pair.of(uri, method));
            return supplier.get();
        } else {
            return getFunctionCache(uri, method);
        }
    }
}

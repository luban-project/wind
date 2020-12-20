package com.lvonce.wind;

import com.lvonce.wind.util.GroovyUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Supplier;



@Slf4j
public class RestFunctionFactory {

    private final static int SRC_CODE_ORDER = 2;
    private final static int SCRIPT_CODE_ORDER = 1;

    private final Map<Pair<String, String>, Pair<Supplier<RestFunction>, Integer>> supplierMap = new LinkedHashMap<>();

    private final Map<Pair<String, String>, RestFunction> functionCache = new LinkedHashMap<>();

    private final Set<String> urls = new LinkedHashSet<>();

    private static final RestFunctionFactory instance = new RestFunctionFactory();

    public static RestFunctionFactory getInstance() {
        return instance;
    }

    private RestFunctionFactory() {}

    public boolean hasFunction(String url, String method) {
        Pair<String, String> key = Pair.of(url, method);
        return supplierMap.containsKey(key);
    }

    private boolean register(String uri, String method, Supplier<RestFunction> supplier, int order) {
        Pair<String, String> key = Pair.of(uri, method);
        Pair<Supplier<RestFunction>, Integer> oldSupplier = supplierMap.get(key);
        if (oldSupplier == null) {
            supplierMap.put(key, Pair.of(supplier, order));
            return true;
        } else if (order > oldSupplier.getSecond()) {
            supplierMap.put(key, Pair.of(supplier, order));
            functionCache.remove(key);
            return true;
        }
        return false;
    }

    private boolean register(String uri, String method, Class<?> clazz, int order) {
        Supplier<RestFunction> supplier = () -> {
            try {
                return (RestFunction) clazz.newInstance();
            } catch (Exception ex) {
                return null;
            }
        };
        return register(uri, method, supplier, order);
    }

    public boolean register(String uri, String method, String lang, String script) {
        if (lang.equalsIgnoreCase("groovy")) {
            Class<?> clazz = GroovyUtil.parseClass(script);
            if (clazz == null) {
                return false;
            }
            if (method.equalsIgnoreCase("ALL")) {
                boolean success = true;
                success &= register(uri, "POST", clazz, SCRIPT_CODE_ORDER);
                success &= register(uri, "PUT", clazz, SCRIPT_CODE_ORDER);
                success &= register(uri, "DELETE", clazz, SCRIPT_CODE_ORDER);
                success &= register(uri, "GET", clazz, SCRIPT_CODE_ORDER);
                return success;
            } else {
                return register(uri, method, clazz, SCRIPT_CODE_ORDER);
            }
        }
        return false;
    }

    public boolean register(String uri, String method, Supplier<RestFunction> supplier) {
        if (method.equalsIgnoreCase("ALL")) {
            boolean success = true;
            success &= register(uri, "POST", supplier, SRC_CODE_ORDER);
            success &= register(uri, "PUT", supplier, SRC_CODE_ORDER);
            success &= register(uri, "DELETE", supplier, SRC_CODE_ORDER);
            success &= register(uri, "GET", supplier, SRC_CODE_ORDER);
            return success;
        } else {
            return register(uri, method, supplier, SRC_CODE_ORDER);
        }
    }




    public RestFunction getFunctionCache(String uri, String method) {
        Pair<String, String> key = Pair.of(uri, method);
        if (functionCache.containsKey(key)) {
            return functionCache.get(key);
        } else {
            Supplier<RestFunction> supplier = supplierMap.get(Pair.of(uri, method)).getFirst();
            RestFunction func = supplier.get();
            functionCache.put(key, func);
            return func;
        }
    }

    public RestFunction getFunction(String uri, String method, boolean isHot) {
        if (isHot) {
            Supplier<RestFunction> supplier = supplierMap.get(Pair.of(uri, method)).getFirst();
            return supplier.get();
        } else {
            return getFunctionCache(uri, method);
        }
    }
}

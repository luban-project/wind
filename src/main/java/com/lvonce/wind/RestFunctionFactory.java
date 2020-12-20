package com.lvonce.wind;

import com.lvonce.wind.util.GroovyUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


/**
 * 应该允许3种类型的函数注册：
 *  源代码Groovy类，通过Annotation扫描获取（可配置热扫描路径）
 *  源代码Java类，通过Annotation扫描获取（可配置热扫描路径）
 *  源代码Config类，通过Annotation扫描获取（可配置热扫描路径）
 *
 *  数据库中Groovy类，通过数据加载获取，可配置加载表和扫描间隔
 *  数据库中Java类，通过数据加载获取，可配置加载表和扫描间隔
 *  数据库中Config类，通过数据加载获取，可配置加载表和扫描间隔
 *
 */
@Slf4j
public class RestFunctionFactory {

    private final static int SRC_CODE_ORDER = 2;
    private final static int SCRIPT_CODE_ORDER = 1;

    private final Map<Pair<String, String>, Pair<Supplier<RestFunction>, Integer>> supplierMap = new LinkedHashMap<>();

    private final Map<Pair<String, String>, RestFunction> functionCache = new LinkedHashMap<>();

    private static final RestFunctionFactory instance = new RestFunctionFactory();

    public static RestFunctionFactory getInstance() {
        return instance;
    }

    private RestFunctionFactory() {}

    public List<String> getUrls() {
        List<String> urls = new ArrayList<>();
        return urls;
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
            return register(uri, method, clazz, SCRIPT_CODE_ORDER);
        }
        return false;
    }

    public boolean register(String uri, String method, Supplier<RestFunction> supplier) {
        return register(uri, method, supplier, SRC_CODE_ORDER);
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

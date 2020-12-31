package com.lvonce.wind.factory;

import com.lvonce.wind.Pair;
import com.lvonce.wind.RestFunction;
import com.lvonce.wind.util.GroovyUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Supplier;



@Slf4j
public class RestFunctionFactoryNormal implements RestFunctionFactory {


    private final Map<String, Supplier<RestFunction>> supplierMap = new LinkedHashMap<>();

    private static final RestFunctionFactoryNormal instance = new RestFunctionFactoryNormal();

    public static RestFunctionFactoryNormal getInstance() {
        return instance;
    }

    private RestFunctionFactoryNormal() {}


    @Override
    public void register(String name, Supplier<RestFunction> supplier) {
        this.supplierMap.put(name, supplier);
    }

    @Override
    public RestFunction getFunction(String name) {
        Supplier<RestFunction> supplier = supplierMap.get(name);
        if (supplier != null) {
            return supplier.get();
        } else {
            return null;
        }
    }
}

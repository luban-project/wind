package com.lvonce.wind.factory;

import com.lvonce.wind.RestFunction;

import java.util.function.Supplier;

public class RestFunctionFactoryCached implements RestFunctionFactory {

    @Override
    public void register(String name, Supplier<RestFunction> supplier) {

    }

    @Override
    public RestFunction getFunction(String name) {
        return null;
    }
}

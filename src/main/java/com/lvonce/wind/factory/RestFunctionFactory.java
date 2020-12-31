package com.lvonce.wind.factory;

import com.lvonce.wind.RestFunction;

import java.util.function.Supplier;

public interface RestFunctionFactory {
    void register(String name, Supplier<RestFunction> supplier);

    RestFunction getFunction(String name);
}

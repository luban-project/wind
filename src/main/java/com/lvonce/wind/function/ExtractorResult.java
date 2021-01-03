package com.lvonce.wind.function;


import java.lang.reflect.Method;

public class ExtractorResult {
    Object[] objects;
    public ExtractorResult(int size) {
        this.objects = new Object[size];
    }

    public void setValue(int idx, Object value) {
        this.objects[idx] = value;
    }

    static ExtractorResult make(ExtractorDef def) {
        return new ExtractorResult(def.extractItems.size());
    }

    public Object apply(Object target, Method method) throws Exception {
        return method.invoke(target, objects);
    }
}

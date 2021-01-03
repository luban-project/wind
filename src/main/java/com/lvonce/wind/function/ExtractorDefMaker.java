package com.lvonce.wind.function;

import com.lvonce.wind.Pair;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExtractorDefMaker {

    public static ExtractorDef makeDef(Method method) throws Exception {
        ExtractorDef extractorDef = new ExtractorDef();
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            RestParam restParam = parameter.getAnnotation(RestParam.class);
            if (restParam != null) {
                extractorDef.addExtractItem(
                        ExtractorDef.ExtractSource.PARAM,
                        restParam.value(),
                        parameter.getType()
                );
                continue;
            }
            RestBody restBody = parameter.getAnnotation(RestBody.class);
            if (restBody != null) {
                extractorDef.addExtractItem(
                        ExtractorDef.ExtractSource.BODY,
                        restBody.value(),
                        parameter.getType()
                );
                continue;
            }
            RestHeader restHeader = parameter.getAnnotation(RestHeader.class);
            if (restHeader != null) {
                extractorDef.addExtractItem(
                        ExtractorDef.ExtractSource.HEADER,
                        restHeader.value(),
                        parameter.getType()
                );
                continue;
            }

            RestPath restPath = parameter.getAnnotation(RestPath.class);
            if (restPath != null) {
                extractorDef.addExtractItem(
                        ExtractorDef.ExtractSource.PATH,
                        restPath.value(),
                        parameter.getType()
                );
                continue;
            }

            RestCookie restCookie = parameter.getAnnotation(RestCookie.class);
            if (restCookie != null) {
                extractorDef.addExtractItem(
                        ExtractorDef.ExtractSource.COOKIE,
                        restCookie.value(),
                        parameter.getType()
                );
                continue;
            }

            throw new Exception("Restful handler function parameter should have annotation");

        }
        return extractorDef;
    }


    public static Map<Pair<String, RestMapping.Method>, Pair<ExtractorDef, Method>> make(Class<?> clazz) throws Exception {
        Map<Pair<String, RestMapping.Method>, Pair<ExtractorDef, Method>> result = new LinkedHashMap<>();
        Annotation annotation = clazz.getAnnotation(RestHandler.class);
        if (annotation == null) {
            return result;
        }

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            RestMapping methodAnnotation = method.getAnnotation(RestMapping.class);
            if (methodAnnotation == null) {
                return null;
            }
            ExtractorDef extractorDef = makeDef(method);
            String url = methodAnnotation.value();

            result.put(
                    Pair.of(url, methodAnnotation.method()),
                    Pair.of(extractorDef, method)
            );
        }
        return result;
    }
}

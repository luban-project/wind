package com.lvonce.wind.test;

import com.lvonce.wind.Pair;
import com.lvonce.wind.function.*;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RestBodyTest {

    @RestHandler
    public static class TestHandler {

        @RestMapping(method = RestMapping.Method.GET, value = "/test")
        public String sayHello(@RestBody("hello") String name) {
            return name;
        }
    }

    List<String> contentList = new ArrayList<>();

    String[] contents = new String[10];

    boolean isSubClass(Class<?> classA, Class<?> classB) {
        try {
            classA.asSubclass(classB);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Test
    public void test() throws Exception {
        Map<Pair<String, RestMapping.Method>, Pair<ExtractorDef, Method>> extractorDefMap = ExtractorDefMaker.make(TestHandler.class);
        Pair<ExtractorDef, Method> pair = extractorDefMap.get(Pair.of("/test", RestMapping.Method.GET));

//        ExtractorResult params = Extractor.extract(null, pair.getFirst());
//        TestHandler handler = new TestHandler();
//        Object result = params.apply(handler, pair.getSecond());

    }

}

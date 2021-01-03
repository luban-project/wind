package com.lvonce.wind;

import java.util.LinkedHashMap;
import java.util.List;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import com.lvonce.wind.http.HttpMethod;

import javax.inject.Inject;


@Slf4j
public class RestRouter {

    @Data
    public static class RouterItem {
        String url;
        String method;
        String handler;
    }

    @Data
    public static class RouterConfig {
        List<RouterItem> routers;
    }

    private final LinkedHashMap<Pair<String, HttpMethod>, String> restBindMap = new LinkedHashMap<>();

    @Inject
    public RestRouter(RouterConfig routerConfig) {
        for (RouterItem item: routerConfig.routers) {
            Pair<String, HttpMethod> key = Pair.of(item.url, HttpMethod.from(item.method));
            restBindMap.put(key, item.handler);
        }
    }

    public void update(RouterConfig routerConfig) {
        restBindMap.clear();
        for (RouterItem item: routerConfig.routers) {
            Pair<String, HttpMethod> key = Pair.of(item.url, HttpMethod.from(item.method));
            restBindMap.put(key, item.handler);
        }
    }


    public boolean hasHandler(String url, HttpMethod method) {
        return this.restBindMap.get(Pair.of(url, method)) != null;
    }

    public String getHandler(String url, HttpMethod method) {
        return this.restBindMap.get(Pair.of(url, method));
    }
}

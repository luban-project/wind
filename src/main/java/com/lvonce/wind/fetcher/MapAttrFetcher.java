package com.lvonce.wind.fetcher;

import java.util.Map;

public class MapAttrFetcher implements AttrFetcher {
    private final Map<String, Object> attrMap;
    private final String attrName;

    public MapAttrFetcher(Map<String, Object> attrMap, String attrName) {
        this.attrMap = attrMap;
        this.attrName = attrName;
    }

    @Override
    public Object fetch() {
        return this.attrMap.get(this.attrName);
    }
}

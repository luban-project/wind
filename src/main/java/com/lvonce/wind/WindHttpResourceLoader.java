package com.lvonce.wind;

import com.lvonce.wind.util.ResourceUtil;
import com.lvonce.wind.util.YamlUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WindHttpResourceLoader {

    public static List<WindHttpResource> loadFromResources(String resourceFileName) {
        String content = ResourceUtil.loadFromResources(resourceFileName);
        Optional<WindHttpResource.WindHttpResourceList> httpResources = YamlUtil.fromYaml(content, WindHttpResource.WindHttpResourceList.class);
        if (httpResources.isPresent()) {
            return httpResources.get().https;
        } else {
            return new ArrayList<>();
        }
    }

    public static WindHttpResource loadFromResource(String resourceFileName) {
        String content = ResourceUtil.loadFromResources(resourceFileName);
        Optional<WindHttpResource> httpResource = YamlUtil.fromYaml(content, WindHttpResource.class);
        if (httpResource.isPresent()) {
            return httpResource.get();
        } else {
            return null;
        }
    }
}

package com.lvonce.wind.test;

import com.lvonce.wind.RestRouter;
import com.lvonce.wind.SQLDataSourceManager;
import com.lvonce.wind.util.ResourceUtil;
import com.lvonce.wind.util.YamlUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ConfigTest {

    @Test
    public void test() {
        List<RestRouter.RouterItem> list = new ArrayList<>();
        RestRouter.RouterItem item1 = new RestRouter.RouterItem();
        item1.setUrl("url1");
        item1.setMethod("GET");
        item1.setHandler("xxx.Hello");
        list.add(item1);

        RestRouter.RouterConfig config1 = new RestRouter.RouterConfig();
        config1.setRouters(list);
        String content = YamlUtil.toYaml(config1).orElse("");
        Assert.assertNotEquals("", content);
        String content2 = ResourceUtil.loadFromResources("routers.yaml");

        RestRouter.RouterConfig config = YamlUtil.fromYaml(content2, RestRouter.RouterConfig.class).orElse(null);
        Assert.assertNotNull(config);
        Assert.assertEquals("GET", config.getRouters().get(0).getMethod());

        String content3 = ResourceUtil.loadFromResources("sql-datasource.yaml");
        SQLDataSourceManager.SQLDataSourceConfig config2 = YamlUtil.fromYaml(content3, SQLDataSourceManager.SQLDataSourceConfig.class).orElse(null);
        Assert.assertNotNull(config2);
    }
}

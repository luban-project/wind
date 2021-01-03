package com.lvonce.wind.test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.lvonce.wind.RestRouter;
import com.lvonce.wind.SQLDataSourceManager;
import com.lvonce.wind.factory.ConfigurationLoader;
import com.lvonce.wind.factory.ConfigurationLoaderModule;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ConfigLoaderTest {

    static class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            bindConstant().annotatedWith(Names.named("router-file"))
                    .to("./src/test/resources/routers.yaml");
            bindConstant().annotatedWith(Names.named("sql-data-source-file"))
                    .to("./src/test/resources/sql-datasource.yaml");
        }
    }


    @Test
    public void test() {
        List<AbstractModule> modules = new ArrayList<>();
        modules.add(new TestModule());
        modules.add(new ConfigurationLoaderModule());
        Injector injector = Guice.createInjector(modules);
        ConfigurationLoader loader = injector.getInstance(ConfigurationLoader.class);
        Assert.assertNotNull(loader);

        RestRouter router1 = injector.getInstance(RestRouter.class);
        Assert.assertNotNull(router1);

        RestRouter router2 = injector.getInstance(RestRouter.class);
        Assert.assertNotNull(router2);
        Assert.assertEquals(router1, router2);


        SQLDataSourceManager manager1 = injector.getInstance(SQLDataSourceManager.class);
        Assert.assertNotNull(manager1);

        SQLDataSourceManager manager2 = injector.getInstance(SQLDataSourceManager.class);
        Assert.assertEquals(manager1, manager2);

    }

}

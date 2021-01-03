package com.lvonce.wind.factory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.lvonce.wind.RestRouter;
import com.lvonce.wind.SQLDataSourceManager;

import javax.inject.Named;
import javax.inject.Singleton;

public class ConfigurationLoaderModule extends AbstractModule {

    @Provides
    @Singleton
    public ConfigurationLoader getConfigurationLoader(
            @Named("router-file") String restRouterFile,
            @Named("sql-data-source-file") String sqlDataSourceFile) {
        return new ConfigurationLoader(restRouterFile, sqlDataSourceFile);
    }

    @Provides
    @Singleton
    public RestRouter getRestRouter(ConfigurationLoader loader) {
        return loader.getRestRouter();
    }

    @Provides
    @Singleton
    public SQLDataSourceManager getSQLDataSourceManager(ConfigurationLoader loader) {
        return loader.getSqlDataSourceManager();
    }
}

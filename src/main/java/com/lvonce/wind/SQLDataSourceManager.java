package com.lvonce.wind;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


@Slf4j
public class SQLDataSourceManager {

    @Data
    public static class SQLDataSourceConfig {
        List<SQLDataSourceConfigItem> sources;
    }

    @Data
    public static class SQLDataSourceConfigItem {
        String name;
        String url;
        String username;
        String password;
        Map<String, Object> properties;
        List<String> tables;
        List<String> data;
    }


    @Getter
    private final LinkedHashMap<String, DataSource> sqlDataSource = new LinkedHashMap<>();

    public void setConfig(SQLDataSourceConfig dataSourceConfig) {
        for (SQLDataSourceConfigItem item : dataSourceConfig.getSources()) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(item.url);
            config.setUsername(item.username);
            config.setPassword(item.password);
            config.addDataSourceProperty("cachePrepStmts", true);
            HikariDataSource ds = new HikariDataSource(config);
            this.sqlDataSource.put(item.name, ds);

            if (item.tables != null) {
                for (String table: item.tables) {
                    executeSql(item.name, table);
                }
            }
            if (item.data != null) {
                for (String d: item.data) {
                    executeSql(item.name, d);
                }
            }
        }
    }

    @Inject
    public SQLDataSourceManager(SQLDataSourceConfig config) {
        setConfig(config);
    }

    public void update(SQLDataSourceConfig config) {
        this.sqlDataSource.clear();
        setConfig(config);
    }

    public void executeSql(String name, String sql) {
        try {
            DataSource ds = this.sqlDataSource.get(name);
            PreparedStatement createTableStmt = ds.getConnection().prepareStatement(sql);
            createTableStmt.executeUpdate();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}

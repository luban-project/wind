package com.lvonce.wind;


import com.lvonce.wind.sql.SqlStatement;

public interface RestSqlContext extends RestContext {
    SqlStatement sql(String sql);
}

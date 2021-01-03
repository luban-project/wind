package com.lvonce.wind.test;

import com.lvonce.wind.RestContext;
import com.lvonce.wind.RestFunction;
import com.lvonce.wind.RestSqlContextImpl;
import com.lvonce.wind.factory.RestFunctionFactoryHot;
import com.lvonce.wind.http.HttpRequest;
import com.lvonce.wind.http.HttpRequestBody;
import com.lvonce.wind.http.HttpResponse;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;


import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.util.LinkedHashMap;
import java.util.Map;

public class RestFunctionHotFactoryTest {

    private DataSource prepareDataSource() throws Exception {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:dbc2m;DATABASE_TO_UPPER=false;MODE=MYSQL");
        config.setUsername("sa");
        config.setPassword("");
        config.addDataSourceProperty("cachePrepStmts", true);
        HikariDataSource ds = new HikariDataSource(config);

        String table = "CREATE TABLE if not exists data_lake(\n" +
                " id int not null,\n" +
                " name varchar(20) null,\n" +
                " age int unsigned null,\n" +
                " primary key (id),\n" +
                " key test_idx_name_age(name, age)\n" +
                ");";

        String data = "INSERT  INTO  data_lake VALUES (?, ?, ?);";

        PreparedStatement createStatement = ds.getConnection().prepareStatement(table);
        createStatement.executeUpdate();

        PreparedStatement insertStatement = ds.getConnection().prepareStatement(data);
        insertStatement.setInt(1, 101);
        insertStatement.setString(2, "wang");
        insertStatement.setInt(3, 23);
        insertStatement.executeUpdate();
        return ds;
    }

    private RestContext initContext(DataSource ds) throws Exception {

        Map<String, DataSource> dataSourceMap = new LinkedHashMap<>();
        dataSourceMap.put("ads", ds);

        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("hello", "hello");
        Map<String, String[]> params = new LinkedHashMap<>();
        params.put("test", new String[]{"wang"});
        InputStream input = new ByteArrayInputStream("{\"test\": \"wang\"}".getBytes());
        HttpRequest request = new HttpRequest(headers, params, new HttpRequestBody(input));
        return new RestSqlContextImpl(dataSourceMap, request.getHeaders(), request.getParams(), request.getBody());
    }


    private void writeToFile(String fileName, String content) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            boolean success = file.createNewFile();
            if (!success) {
                throw new IOException("create fail");
            }
        }
        FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8);
    }

    private void prepareSource() throws IOException {
        String content = "package com.xxx.example;\n" +
                "import com.google.inject.Inject;\n" +
                "import com.google.inject.name.Names;\n" +
                "import com.google.inject.name.Named;\n" +
                "\n" +
                "public class Depen1 {\n" +
                "    String name;\n" +
                "\n" +
                "    @Inject\n" +
                "    public Depen1(@Named(\"testName\") String name) {\n" +
                "        this.name = name;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public String getName() {\n" +
                "        return name;\n" +
                "    }\n" +
                "\n" +
                "    public void setName(String name) {\n" +
                "        this.name = name;\n" +
                "    }\n" +
                "}\n";
        writeToFile("./testHot2/Depen1.java", content);

        content = "package com.xxx.example;\n" +
                "\n" +
                "import com.xxx.example.Depen1;\n" +
                "import com.google.inject.AbstractModule;\n" +
                "import com.google.inject.Provides;\n" +
                "import com.google.inject.name.Names;\n" +
                "import com.google.inject.name.Named;\n" +
                "\n" +
                "public class HelloModule extends AbstractModule {\n" +
                "    @Override\n" +
                "    protected void configure() {\n" +
                "        bindConstant().annotatedWith(Names.named(\"testName\")).to(\"wang\");\n" +
                "    }\n" +
                "}\n";

        writeToFile("./testHot2/HelloModule.java", content);


        content = "package com.xxx.example;\n" +
                "\n" +
                "import com.lvonce.wind.RestContext;\n" +
                "import com.lvonce.wind.RestSqlContext;\n" +
                "import com.lvonce.wind.http.HttpResponse;\n" +
                "import com.lvonce.wind.sql.IsolationLevel;\n" +
                "import com.lvonce.wind.sql.Transaction;\n" +
                "import com.lvonce.wind.sql.TransactionFunc;\n" +
                "import com.lvonce.wind.sql.TransactionResult;\n" +
                "import com.lvonce.wind.sql.statment.SqlStatement;\n" +
                "\n" +
                "\n" +
                "import com.lvonce.wind.sql.statment.SqlStatement;\n" +
                "\n" +
                "import java.sql.SQLException;\n" +
                "import java.util.LinkedHashMap;\n" +
                "import java.util.List;\n" +
                "import java.util.Map;\n" +
                "import com.google.inject.Inject;\n" +
                "import com.lvonce.wind.RestRouter;\n" +
                "\n" +
                "public class HelloRest implements com.lvonce.wind.RestFunction {\n" +
                "\n" +
                "    @Inject\n" +
                "    private Depen1 depen1;\n" +
                "\n" +
                "    @Inject\n" +
                "    private RestRouter restRouter;\n" +
                "\n" +
                "\n" +
                "    @Override\n" +
                "    public void applyGet(RestContext ctx) throws Exception {\n" +
                "        RestSqlContext sqlCtx = (RestSqlContext) ctx;\n" +
                "        Map<String, Object> body = ctx.getRequest().getBody().asMap();\n" +
                "        Map<String, String[]> params = ctx.getRequest().getParams();\n" +
                "        Map<String, Object> sqlMap = new LinkedHashMap<>();\n" +
                "        sqlMap.put(\"test\", params.get(\"test\")[0]);\n" +
                "\n" +
                "        TransactionResult result;\n" +
                "\n" +
                "        TransactionFunc func = (Transaction trx) -> {\n" +
                "            SqlStatement sql = trx.sql(\"select * from data_lake where name = :test\");\n" +
                "            List<Map<String, Object>> sqlResult = sql.query(sqlMap);\n" +
                "            return TransactionResult.of(true, sqlResult.size(), sqlResult);\n" +
                "        };\n" +
                "        result = sqlCtx.trx(\"ads\", IsolationLevel.NONE).apply(func);\n" +
                "        HttpResponse response = ctx.getResponse();\n" +
                "        response.setBody(result.getBody());\n" +
                "        response.setErrCode(\"0\");\n" +
                "        System.out.println(\"name: \"+depen1.getName());\n" +
                "        response.setErrMessage(depen1.getName());\n" +
                "    }\n" +
                "}";
        writeToFile("./testHot2/HelloRest.java", content);

    }

    private void updateResource() throws IOException {
        String content = "package com.xxx.example;\n" +
                "\n" +
                "import com.xxx.example.Depen1;\n" +
                "import com.google.inject.AbstractModule;\n" +
                "import com.google.inject.Provides;\n" +
                "import com.google.inject.name.Names;\n" +
                "import com.google.inject.name.Named;\n" +
                "\n" +
                "public class HelloModule extends AbstractModule {\n" +
                "    @Override\n" +
                "    protected void configure() {\n" +
                "        bindConstant().annotatedWith(Names.named(\"testName\")).to(\"wang2\");\n" +
                "    }\n" +
                "}\n";

        writeToFile("./testHot2/HelloModule.java", content);
    }

    @Test
    public void test() throws Exception {
        File file = new File("./testHot2");
        if (!file.exists()) {
            file.mkdir();
        }
        DataSource ds = prepareDataSource();
        prepareSource();

        RestFunctionFactoryHot factory = new RestFunctionFactoryHot();
        factory.scanDir("./testHot2");
        factory.watchDirToCompile("name", "./testHot2");

        RestFunction func = factory.getFunction("com.xxx.example.HelloRest");

        RestContext ctx = initContext(ds);
        func.applyGet(ctx);
        HttpResponse response = ctx.getResponse();
        Assert.assertNotNull(response);
        Assert.assertEquals("wang", response.getErrMessage());

//        updateResource();
//        Thread.sleep(2000);
//
//        func = factory.getFunction("com.xxx.example.HelloRest");
//        ctx = initContext(ds);
//        func.applyGet(ctx);
//        response = ctx.getResponse();
//        Assert.assertNotNull(response);
//        Assert.assertEquals("wang2", response.getErrMessage());
    }
}

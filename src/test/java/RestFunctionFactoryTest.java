import com.lvonce.wind.RestFunction;
import com.lvonce.wind.RestFunctionFactory;
import com.lvonce.wind.RestSqlContext;
import com.lvonce.wind.RestSqlContextImpl;
import com.lvonce.wind.http.HttpResponse;
import com.lvonce.wind.util.JsonUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import groovy.util.logging.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class RestFunctionFactoryTest {

    private String script = "" +
            "class Person implements RestSqlFunction  { \n" +
            "   void applyGet(RestSqlContext ctx) {\n" +
            "   def body = ctx.request.body.asMap();"+
            "   if (ctx.request.headers['hello'] == 'hello' && body['test'] == 'wang')  {" +
            "      def sql = ctx.sql('select * from data_lake where name = :test');"+
            "      def result = sql.query(body);"+
            "      ctx.response.body = result" +
            "    } " +
            "  }"+
            "}";

    private String table = "CREATE TABLE data_lake(\n" +
            " id int not null,\n" +
            " name varchar(20) null,\n" +
            " age int unsigned null,\n" +
            " primary key (id),\n" +
            " key test_idx_name_age(name, age)\n" +
            ");";

    private String data = "INSERT  INTO  data_lake VALUES (?, ?, ?);";

    private DataSource getH2DataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:dbc2m;DATABASE_TO_UPPER=false;MODE=MYSQL");
        config.setUsername("sa");
        config.setPassword("");
        config.addDataSourceProperty("cachePrepStmts", true);
        HikariDataSource ds = new HikariDataSource(config);
        try {
            PreparedStatement createTableStmt = ds.getConnection().prepareStatement(table);
            createTableStmt.executeUpdate();

            PreparedStatement stmt = ds.getConnection().prepareStatement(data);
            stmt.setInt(1, 101);
            stmt.setString(2, "wang");
            stmt.setInt(3, 23);
            int result = stmt.executeUpdate();
            System.out.println(result);


        } catch (Exception ex) {
            System.out.println(ex);
        }
        return ds;
    }

    private String getContent() {
        return "{\"test\": \"wang\"}";
    }

    @Test
    public void test() {
        RestFunctionFactory factory = RestFunctionFactory.getInstance();
        factory.register("test", "test", "groovy", script);
        factory.register("test1", "test1", RestTestFunction::new);

        DataSource dataSource = getH2DataSource();
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("hello", "hello");


        Map<String, String[]> params = new LinkedHashMap<>();

        InputStream input = new ByteArrayInputStream(getContent().getBytes());

        RestSqlContext context = new RestSqlContextImpl(dataSource, headers, params, input);
        RestFunction func = factory.getFunction("test", "test", false);
        func.applyGet(context);
        HttpResponse response = context.getResponse();
        String result = JsonUtil.toJson(response.getBody(), "");
        Assert.assertEquals(result, "[{\"id\":101,\"name\":\"wang\",\"age\":23}]");

    }
}
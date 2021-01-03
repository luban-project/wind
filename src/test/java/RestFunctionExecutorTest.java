//import com.lvonce.wind.RestFunctionExecutor;
//import com.lvonce.wind.factory.RestFunctionFactoryNormal;
//import com.lvonce.wind.http.HttpMethod;
//import com.lvonce.wind.http.HttpRequest;
//import com.lvonce.wind.http.HttpRequestBody;
//import com.lvonce.wind.http.HttpResponse;
//import org.junit.Assert;
//import org.junit.Test;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//public class RestFunctionExecutorTest {
//
//    @Test
//    public void test() throws Exception {
//        RestFunctionFactoryNormal factory = RestFunctionFactoryNormal.getInstance();
////        factory.register("/hello", "ALL", RestTestFunction::new);
//
//
//        RestFunctionExecutor executor = new RestFunctionExecutor(factory);
//        executor.registerSQLDataSource("ads", "jdbc:h2:mem:dbc2m;DATABASE_TO_UPPER=false;MODE=MYSQL", "sa", "");
//        String createTableSql = "CREATE TABLE data_lake(\n" +
//                " id int not null,\n" +
//                " name varchar(20) null,\n" +
//                " age int unsigned null,\n" +
//                " primary key (id),\n" +
//                " key test_idx_name_age(name, age)\n" +
//                ");";
//        executor.prepareSQLDataSource("ads", createTableSql);
//        String insertDataSql = "INSERT  INTO  data_lake VALUES (1, 'wang', 23);";
//        executor.prepareSQLDataSource("ads", insertDataSql);
//
//        boolean shouldIntercept = executor.shouldIntercept("/hello", HttpMethod.from("GET"));
//        Assert.assertTrue(shouldIntercept);
//
//
//        Map<String, String> headers = new LinkedHashMap<>();
//        headers.put("hello", "hello");
//        Map<String, String[]> params = new LinkedHashMap<>();
//        InputStream input = new ByteArrayInputStream("{\"test\": \"wang\"}".getBytes());
//        HttpRequest request = new HttpRequest(headers, params, new HttpRequestBody(input));
//
//        HttpResponse response = executor.apply("/hello", "GET", request);
//        Assert.assertNotNull(response);
//
//    }
//}

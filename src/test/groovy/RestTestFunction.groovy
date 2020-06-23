import com.lvonce.wind.RestSqlContext
import com.lvonce.wind.RestSqlFunction

class RestTestFunction implements RestSqlFunction  {
       void applyGet(RestSqlContext ctx) {
           def body = ctx.request.body.asMap();
           if (ctx.request.headers['hello'] == 'hello' && body['test'] == 'wang')  {
                def sql = ctx.sql('select * from data_lake where name = :test');
                def result = sql.query(body);
                ctx.response.body = result
           }
       }
}

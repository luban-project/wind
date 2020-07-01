import com.lvonce.wind.RestSqlContext
import com.lvonce.wind.RestSqlFunction
import com.lvonce.wind.sql.IsolationLevel
import com.lvonce.wind.sql.TransactionResult

class RestTestFunction implements RestSqlFunction  {
       void applyGet(RestSqlContext ctx) {
           def body = ctx.request.body.asMap();
           if (ctx.request.headers['hello'] == 'hello' && body['test'] == 'wang')  {
                assert  body['test'] == 'wang2'
                def trxHandler = ctx.trx(IsolationLevel.REPEATABLE_READ)
                def result = trxHandler.apply({ trx->
                    def sql = trx.sql('select * from data_lake where name = :test')
                    def result = sql.query(body);
                    return TransactionResult.of(true, result.size(), result)
                })
                ctx.response.body = result.getBody()
           }
       }
}

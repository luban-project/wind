import com.lvonce.wind.RestSqlContext
import com.lvonce.wind.RestSqlFunction
import com.lvonce.wind.sql.IsolationLevel
import com.lvonce.wind.sql.TransactionResult

class RestTestFunction implements RestSqlFunction  {
       void applyGet(RestSqlContext ctx) {
           def body = ctx.request.body.asMap();
           if (ctx.request.headers['hello'] == 'hello' && body['test'] == 'wang')  {

//                assert body['test'] == 5
//                assert body['sdfsdf'] > 15
//                assert body['sdfjsldjfljdsf'] == 'sdfjsdf'
//                assert ctx.request.headers['headfs'] < 0.5

                def binding = ['name': 'wang']
                def str = '''
                select * from 

'''
                def engine = new groovy.text.SimpleTemplateEngine()
                def template = engine.createTemplate(str).make(binding)
                def resultStr = template.toString();



                def result = ctx.trx('ads', IsolationLevel.NONE).apply({ trx->
                    def sql = trx.sql('select * from data_lake where name = :test')
                    def result = sql.query(body);
                    return TransactionResult.of(true, result.size(), result)
                })
                ctx.response.body = result.getBody()
                ctx.response.errMessage = ''
           }
       }
}

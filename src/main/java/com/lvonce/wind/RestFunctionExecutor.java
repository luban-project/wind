package com.lvonce.wind;


import com.lvonce.wind.factory.RestFunctionFactory;
import com.lvonce.wind.http.HttpMethod;
import com.lvonce.wind.http.HttpRequest;
import com.lvonce.wind.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;


/**
 * 1. register data source
 * 2. register function factory
 * 3. apply function on data source
 *
 *
 */
@Slf4j
public class RestFunctionExecutor {

    private final SQLDataSourceManager dataSourceManager;
    private final RestRouter restRouter;
    private final RestFunctionFactory factory;

    public RestFunctionExecutor(
            SQLDataSourceManager dataSourceManager,
            RestRouter restRouter,
            RestFunctionFactory factory) {
        this.dataSourceManager = dataSourceManager;
        this.restRouter = restRouter;
        this.factory = factory;
    }

    public boolean shouldIntercept(String url, HttpMethod method) {
        return this.restRouter.hasHandler(url, method);
    }

    public HttpResponse apply(String url, HttpMethod method, HttpRequest request) throws Exception {
        RestSqlContext context = new RestSqlContextImpl(this.dataSourceManager.getSqlDataSource(), request.getHeaders(), request.getParams(), request.getBody());
        String handlerName = this.restRouter.getHandler(url, method);
        RestFunction func = factory.getFunction(handlerName);
        func.apply(method.toString(), context);
        return context.getResponse();
    }

}

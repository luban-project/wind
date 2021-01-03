package com.lvonce.wind;

import com.lvonce.wind.RestFunctionExecutor;
import com.lvonce.wind.factory.RestFunctionFactory;
import com.lvonce.wind.http.HttpMethod;
import com.lvonce.wind.http.HttpRequest;
import com.lvonce.wind.http.HttpRequestBody;
import com.lvonce.wind.http.HttpResponse;
import com.lvonce.wind.util.JsonUtil;
import groovy.util.logging.Slf4j;
import lombok.AllArgsConstructor;
import org.junit.Assert;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class WindWebFilter implements Filter {

    private final RestFunctionExecutor executor;

    private HttpRequest translate(HttpServletRequest httpServletRequest) throws IOException {
        Map<String, String> headers = new LinkedHashMap<>();
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String header = httpServletRequest.getHeader(headerName);
            headers.put(headerName, header);
        }
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        HttpRequestBody body = new HttpRequestBody(httpServletRequest.getInputStream());
        return new HttpRequest(headers, parameterMap, body);
    }


    private String stringify(HttpResponse response) {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        if (response.getErrCode().equalsIgnoreCase("0")) {
            responseMap.put("success", true);
            responseMap.put("errCode", response.getErrCode());
            responseMap.put("errMessage", response.getErrMessage());
            responseMap.put("data", response.getBody());
        } else {
            responseMap.put("success", false);
            responseMap.put("errCode", response.getErrCode());
            responseMap.put("errMessage", response.getErrMessage());
        }
        return JsonUtil.toJson(responseMap,"");
    }

    private void output(HttpServletResponse servletResponse, HttpResponse response) throws IOException {
        Map<String, String> headers = response.getHeaders();
        for (Map.Entry<String, String> entry: headers.entrySet()) {
            servletResponse.addHeader(entry.getKey(), entry.getValue());
        }

        Map<String, String> cookieMap = response.getCookies();
        for (Map.Entry<String, String> entry: cookieMap.entrySet()) {
            Cookie cookie = new Cookie(entry.getKey(), entry.getValue());
            servletResponse.addCookie(cookie);
        }

        servletResponse.setCharacterEncoding("UTF-8");
        servletResponse.setContentType("application/json; charset=utf-8");
        Writer writer = servletResponse.getWriter();
        writer.write(stringify(response));
        writer.flush();
        writer.close();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            System.out.println(request.getLocalAddr());
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String url = httpServletRequest.getRequestURI();

            String method = httpServletRequest.getMethod().toUpperCase();
            boolean shouldIntercept = executor.shouldIntercept(url, HttpMethod.from(method));
            if (shouldIntercept) {
                HttpRequest funcRequest = translate(httpServletRequest);
                HttpResponse funcResponse = executor.apply(url, HttpMethod.from(method), funcRequest);
                output((HttpServletResponse) response, funcResponse);
            } else {
                System.out.println(String.format("url: %s, not to intercept", url));
                chain.doFilter(request, response);
            }
        } catch (Exception ex) {
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setErrCode("InternalServerError");
            httpResponse.setErrMessage(ex.getMessage());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            Writer writer = response.getWriter();
            writer.write(stringify(httpResponse));
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {

    }
}

package com.wp.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.UUID;

/**
 * @Classname TraceIdFilter
 * @Description TraceId过滤器
 * @Date 2021/2/19 10:26
 * @Created by wangpeng116
 */
//拦截所有请求
@WebFilter(urlPatterns = "/*", asyncSupported = true)
//第一个经过的过滤器
@Order(1)
public class TraceIdFilter implements Filter {
    private static final String TRACE_ID = "traceId";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //暂不需要处理
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //尝试从请求信息中获取TraceId信息
        String traceId = request.getParameter(TRACE_ID);
        if (StringUtils.isEmpty(traceId)) {
            //为空设置默认值
            traceId = UUID.randomUUID().toString();
        }
        //TreadLocal对象，当前线程绑定的map
        MDC.put(TRACE_ID, traceId);
        //继续
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        //暂不需要处理
    }
}

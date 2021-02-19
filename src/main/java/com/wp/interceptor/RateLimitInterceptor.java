package com.wp.interceptor;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname RateLimitInterceptor
 * @Description 全局限流器
 * @Date 2021/2/19 10:00
 * @Created by wangpeng116
 */
@Configuration
@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {
    //限流器每秒限制10次请求(即允许10个请求访问 QPS=10)
    private static final RateLimiter rateLimiter = RateLimiter.create(10);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //尝试获取令牌
        if (!rateLimiter.tryAcquire()) {
            log.error("系统被限流");
            return false;
        } else {
            //没有被限流，正常访问
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        //无需做处理
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //无需做处理
    }
}

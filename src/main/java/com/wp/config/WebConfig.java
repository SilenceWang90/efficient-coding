package com.wp.config;

import com.wp.interceptor.RateLimitInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

/**
 * @Classname WebConfig
 * @Description Web配置类
 * @Date 2021/2/19 10:06
 * @Created by wangpeng116
 */
@Configuration
@Slf4j
public class WebConfig extends WebMvcConfigurerAdapter {
    /**
     * 限流拦截器
     */
    @Resource
    private RateLimitInterceptor rateLimitInterceptor;

    /**
     * 添加拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册拦截器
        registry.addInterceptor(rateLimitInterceptor)
                //拦截器拦截的地址
                .addPathPatterns("/**");
    }

    /**
     * 添加静态资源，不会被拦截器处理
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    }
}

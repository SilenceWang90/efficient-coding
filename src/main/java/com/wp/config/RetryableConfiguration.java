package com.wp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

/**
 * @Classname RetryableConfiguration
 * @Description 重试配置类
 * @Date 2021/4/12 16:17
 * @Created by wangpeng116
 */
@Configuration
@EnableRetry
public class RetryableConfiguration {
}

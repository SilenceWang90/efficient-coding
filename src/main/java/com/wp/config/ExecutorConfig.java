package com.wp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Classname ExecutorConfig
 * @Description excel导出功能线程池
 * @Date 2021/2/20 14:08
 * @Created by wangpeng116
 */
@Configuration
@EnableAsync
@Slf4j
public class ExecutorConfig {
    /**
     * 定义导出服务线程池
     *
     * @return
     */
    @Bean("exportServiceExecutor")
    public Executor exportServiceExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数量：当前机器的核心数
        threadPoolTaskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        // 最大线程数量：一般设置为核心线程数的2倍
        threadPoolTaskExecutor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        // 阻塞队列大小：默认为整型最大值
        threadPoolTaskExecutor.setQueueCapacity(Integer.MAX_VALUE);
        // 线程池中的线程名前缀
        threadPoolTaskExecutor.setThreadNamePrefix("export-");
        // 拒绝策略：默认策略，直接拒绝
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 线程池初始化
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}

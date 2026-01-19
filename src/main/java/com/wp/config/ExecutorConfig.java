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
 * @Description 线程池配置
 * @Date 2021/2/20 14:08
 * @Created by wangpeng116
 */
@Configuration
@EnableAsync
@Slf4j
public class ExecutorConfig {

    /**
     * 核心线程数
     */
    private static final int CORE_POOL_SIZE = 10;

    /**
     * 最大线程数
     * 允许突发流量时的临时扩容
     */
    private static final int MAX_POOL_SIZE = 20;

    /**
     * 队列容量
     * 一般内存32G非常充裕，可以适当加大队列缓冲突发流量
     * 但不宜无界，避免OOM
     */
    private static final int QUEUE_CAPACITY = 1000;

    /**
     * 线程空闲时间 (秒)
     * 超过核心线程数的线程，空闲多久后销毁
     */
    private static final int KEEP_ALIVE_SECONDS = 60;

    /**
     * 线程名前缀
     * 方便日志排查问题
     */
    private static final String THREAD_NAME_PREFIX = "wangpeng-";


    /**
     * 常规服务线程池
     *
     * @return
     */
    @Bean(name = "commonTaskExecutor")
    public ThreadPoolTaskExecutor asyncThreadExecutor() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        //线程池 核心线程数
        threadPool.setCorePoolSize(CORE_POOL_SIZE);
        //线程池 最大线程数
        threadPool.setMaxPoolSize(MAX_POOL_SIZE);
        //队列大小
        threadPool.setQueueCapacity(QUEUE_CAPACITY);
        //活跃时间
        threadPool.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
        //线程名前缀
        threadPool.setThreadNamePrefix(THREAD_NAME_PREFIX);
        log.info("init asyncThreadPool. corePoolSize:{}, maxPoolSize:{}, queueCapacity:{}, keepAliveSeconds:{}",
                CORE_POOL_SIZE, MAX_POOL_SIZE, QUEUE_CAPACITY, KEEP_ALIVE_SECONDS);
        // 当池满且队列满时，由调用线程直接执行
        threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPool.initialize();
        return threadPool;
    }


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

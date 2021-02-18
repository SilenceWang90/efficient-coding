package com.wp.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Classname CaffeineCacheConfig
 * @Description 缓存管理器
 * @Date 2021/2/18 16:02
 * @Created by wangpeng116
 */
@Configuration
//开启Springboot的缓存功能
@EnableCaching
@Slf4j
public class CaffeineCacheConfig {
    /**
     * CacheManager实现
     *
     * @return
     */
    @Bean("cacheManager")
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<CaffeineCache> caches = Lists.newArrayList();
        //创建缓存（缓存名称，缓存配置）
        caches.add(new CaffeineCache("users-cache", Caffeine.newBuilder()
                //指定Key西安的最大缓存数据量
                .maximumSize(1000)
                //最后一次访问之后，多久过期
                .expireAfterAccess(120, TimeUnit.SECONDS)
                .build()));
        cacheManager.setCaches(caches);
        return cacheManager;
    }
}

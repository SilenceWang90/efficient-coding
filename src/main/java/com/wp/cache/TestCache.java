package com.wp.cache;

import com.wp.validation.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname TestCache
 * @Description 缓存测试
 * @Date 2021/2/18 16:36
 * @Created by wangpeng116
 */
@RestController
@RequestMapping("/testCache")
@Slf4j
public class TestCache {
    @PostMapping("/test1")
    @Cacheable(cacheNames = "users-cache", key = "#user.userId", condition = "#user.userName=='wangpeng'")
    public String test1(@RequestBody UserInfo user) {
        log.info("未使用缓存");
        return "wp";
    }
}

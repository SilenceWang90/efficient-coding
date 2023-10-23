package com.wp.feign.feignapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Description
 * @Author wangpeng
 * @Date 2023/10/23 15:20
 */
@FeignClient(name = "test-service", path = "/test", url = "http://localhost:8080")
public interface TestServerApi {
    @GetMapping("/test1")
    void test1();
}

package com.wp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Classname EfficientCoding
 * @Description 启动类
 * @Date 2021/1/5 13:47
 * @Created by wangpeng116
 */
@EnableFeignClients(basePackages = "com.wp.feign.feignapi")
@SpringBootApplication
//扫秒自定义的filter
@ServletComponentScan
@Slf4j
public class EfficientCodingApplication {
    public static void main(String[] args) {
        SpringApplication.run(EfficientCodingApplication.class, args);
    }
}

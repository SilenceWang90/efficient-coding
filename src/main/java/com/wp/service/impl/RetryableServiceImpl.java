package com.wp.service.impl;

import com.wp.service.RetryableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

/**
 * @Classname RetryableServiceImpl
 * @Description 重试服务实现
 * @Date 2021/4/12 16:14
 * @Created by wangpeng116
 */
@Service
@Slf4j
public class RetryableServiceImpl implements RetryableService {
    @Retryable(maxAttempts = 3, value = {ArithmeticException.class}, backoff = @Backoff(delay = 2000L, multiplier = 2))
    @Override
    public String testRetry(String text) {
        log.info("我被调用：{}", LocalTime.now());
        // 空指针
        /*UserFillData userFillData = new UserFillData();
        userFillData.getUserId().toString();*/
        // 计算异常
        int n = 1 / 0;
        return "重试成功了么：" + text;
    }

    @Recover
    public String testRetryRecovery(ArithmeticException e) {
        log.error("发生异常：", e);
        return "recover";
    }
}
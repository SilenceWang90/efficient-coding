package com.wp.service;

/**
 * @Classname RetryableService
 * @Description 重试服务
 * @Date 2021/4/12 16:14
 * @Created by wangpeng116
 */
public interface RetryableService {
    /**
     * 重试服务
     * @param text
     * @return
     */
    String testRetry(String text);

    /**
     * 重试失败回调
     * @param text
     */
    String testRecover(String text);
}

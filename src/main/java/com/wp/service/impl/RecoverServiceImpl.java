package com.wp.service.impl;

import com.wp.service.RecoverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Recover;

/**
 * @Classname RecoverServiceImpl
 * @Description TODO
 * @Date 2021/4/12 18:37
 * @Created by wangpeng116
 */
@Slf4j
public class RecoverServiceImpl implements RecoverService {
    @Override
    @Recover
    public String recoverMethod(ArithmeticException e) {
        log.error("异常信息：", e);
        return "recover成功";
    }
}

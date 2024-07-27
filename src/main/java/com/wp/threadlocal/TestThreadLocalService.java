package com.wp.threadlocal;

import org.springframework.stereotype.Service;

/**
 * @author wangpeng
 * @description TestThreadLocalService
 * @date 2024/7/13 13:44
 **/
@Service
public class TestThreadLocalService {
    public ThreadLocalHolder.UserInfo printInfo() {
        Runnable runnable = () -> {
            System.out.println("我是子线程");
            System.out.println("我是子线程的输出：" + ThreadLocalHolder.getUserInfo());
        };
        new Thread(runnable).start();
        return ThreadLocalHolder.getUserInfo();
    }
}

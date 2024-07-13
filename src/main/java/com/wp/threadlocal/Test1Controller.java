package com.wp.threadlocal;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wangpeng
 * @description Test1Controller
 * @date 2024/7/13 11:19
 **/
@RestController
@RequestMapping("/test1")
public class Test1Controller {
    @Resource
    private TestThreadLocalService testThreadLocalService;

    @RequestMapping("say")
    public ThreadLocalHolder.UserInfo say(@RequestParam("timeout") long timeout
            , @RequestBody ThreadLocalHolder.UserInfo userInfo) throws InterruptedException {
        ThreadLocalHolder.setUserInfo(userInfo);
        Thread.sleep(timeout);
        return testThreadLocalService.printInfo();
    }
}

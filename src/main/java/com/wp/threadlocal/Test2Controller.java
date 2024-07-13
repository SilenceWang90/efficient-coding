package com.wp.threadlocal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangpeng
 * @description Test2Controller
 * @date 2024/7/13 11:20
 **/
@RestController
@RequestMapping("/test2")
public class Test2Controller {
    @RequestMapping("say")
    public String say() throws InterruptedException {
        return "sayTest2";
    }
}

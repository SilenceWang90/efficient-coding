package com.wp.applicationevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangpeng
 * @description Springboot监听事件异步处理时，和主线程事物强关联的方法测试controller
 * @date 2026/1/28 17:41
 **/
@RestController
@RequestMapping("/myEvent")
public class MyEventController {
    @Autowired
    private PublishMyEventService publishMyEventService;

    @RequestMapping("/publishMyEvent")
    public String publishMyEvent() throws InterruptedException {
        String result = publishMyEventService.publishMyEvent();
        return "发布事件方法返回内容：" + result;

    }
}

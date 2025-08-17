package com.wp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author wangpeng
 * @description SseController
 * @date 2025/8/16 07:52
 **/
@RestController
@RequestMapping("/sseStudy")
public class SseController {
    @GetMapping("/test1")
    public SseEmitter test1() {
        // 创建长链接对象
        SseEmitter sseEmitter = new SseEmitter(10000L);
        /** 链接出现异常时触发 **/
        sseEmitter.onError(exception -> System.out.println("SSE连接发生异常"));

        /** 链接超时时触发 **/
        sseEmitter.onTimeout(() -> System.out.println("SSE连接超时，主动关闭"));

        /** 当SSE连接正常完成（主动调用complete()方法或客户端关闭连接）时触发 **/
        sseEmitter.onCompletion(() -> System.out.println("SSE 连接正常结束"));
        try {
            // 发送内容构造器
            SseEmitter.SseEventBuilder sseEventBuilder = SseEmitter.event().id("event_id-1")
                    .name("message")
                    // 当链接断开后，允许客户端在指定时间后进行重连，避免频繁重连造成服务器压力
                    .reconnectTime(5000L)
                    .data("我是你想要的信息，这次是我的SseEmitter学习，希望以后可以把websocket也学好，加油！！！");
            // 参数说明
            sseEmitter.send(sseEventBuilder);
        } catch (Exception ex) {
            // 主动终止并传递异常，并会立即触发 onError() 注册的回调逻辑
            sseEmitter.completeWithError(ex);
        } finally {
            /** 主动标记SSE连接完成，关闭连接并触发onCompletion回调 **/
            sseEmitter.complete();
        }
        System.out.println("Sse接口方法执行完成");
        return sseEmitter;
    }
}

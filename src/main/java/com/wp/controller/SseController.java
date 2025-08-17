package com.wp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        /** 1、创建长链接对象 **/
        SseEmitter sseEmitter = new SseEmitter(20000L);
        // 1.1、链接出现异常时触发
        sseEmitter.onError(exception -> {
            System.out.println("SSE连接发生异常");
            sseEmitter.complete();
        });
        // 1.2、 链接超时时触发
        sseEmitter.onTimeout(() -> {
            System.out.println("SSE连接超时，主动关闭");
            sseEmitter.complete();
        });
        // 1.3、 当SSE连接正常完成（主动调用complete()方法或客户端关闭连接）时触发
        sseEmitter.onCompletion(() -> System.out.println("SSE连接正常结束"));

        /** 2、异步处理。切记不能和controller接口同步处理，否则就是常规的一次性返回，达不到流式返回以及长链接的效果了 **/
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(() -> {
            try {
                // 2.1、为了看出流式处理效果，循环输出，这样可以在客户端看出来输出是一段一段输出的
                for (int i = 0; i < 3; i++) {
                    // 发送内容构造器
                    SseEmitter.SseEventBuilder sseEventBuilder = SseEmitter.event().id("event_id-" + i)
                            .name("message")
                            // 当链接断开后，允许客户端在指定时间后进行重连，避免频繁重连造成服务器压力
                            .reconnectTime(5000L)
                            .data("我是你想要的信息，这次是我的SseEmitter学习，希望以后可以把websocket也学好，加油！！！");
                    // 参数说明
                    sseEmitter.send(sseEventBuilder);
                    Thread.sleep(1500L);
                }
            } catch (Exception ex) {
                // 主动终止并传递异常，并会立即触发 onError() 注册的回调逻辑
                sseEmitter.completeWithError(ex);
            } finally {
                /** 主动标记SSE连接完成，关闭连接并触发onCompletion回调 **/
                sseEmitter.complete();
            }
        });
        System.out.println("Sse接口方法执行完成");
        // 返回SseEmitter对象用于和客户端创建长链接
        return sseEmitter;
    }
}

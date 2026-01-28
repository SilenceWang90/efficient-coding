package com.wp.applicationevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author wangpeng
 * @description PublishMyEventService
 * @date 2026/1/28 17:42
 **/
@Service
public class PublishMyEventService {
    @Autowired
    private ApplicationEventPublisher publisher;

    @Transactional(rollbackFor = Exception.class)
    public String publishMyEvent() throws InterruptedException {
        /** 1、确认事物是否存在并激活 **/
        System.out.println("当前事务名称：" + TransactionSynchronizationManager.getCurrentTransactionName());
        System.out.println("事务是否激活：" + TransactionSynchronizationManager.isActualTransactionActive());
        /** 2、构造发布事件的对象 **/
        MyEvent myEvent = new MyEvent();
        myEvent.setName("wangpeng");
        myEvent.setAge(18);
        /** 3、发布事件 **/
        publisher.publishEvent(myEvent);
        /** 4、模仿发布事件后方法不停止，继续执行。明确事物方法不提交，监听方法不会开始执行 **/
        System.out.println("事件已发布");
        Thread.sleep(5000);
        System.out.println("发布事件方法执行完成");
        Thread.sleep(5000);
        /** 验证事物异常回滚时，监听器执行的情况 **/
//        int i = 1/0;
        return "发布事件方法执行完成";
    }
}

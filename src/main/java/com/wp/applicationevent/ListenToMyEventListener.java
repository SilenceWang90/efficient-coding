package com.wp.applicationevent;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author wangpeng
 * @description ListenToMyEventListener
 * @date 2026/1/28 17:42
 **/
@Component
public class ListenToMyEventListener {
    /**
     * 当事务成功提交后，此方法才会执行
     * @param myEvent
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleMyEvent(MyEvent myEvent) {
        System.out.println("------" + "handleMyEvent监听方法开始执行" + "------");
        System.out.println("handleMyEvent监听myEvent的内容为：" + myEvent);
        System.out.println("------" + "handleMyEvent监听方法结束执行" + "------");
    }

    /**
     * 当事务回滚后，此方法才会执行
     * @param myEvent
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    @Async
    public void handleMyEventRollback(MyEvent myEvent) {
        System.out.println("------" + "handleMyEventRollback监听方法开始执行" + "------");
        System.out.println("handleMyEventRollback监听myEvent的内容为：" + myEvent);
        System.out.println("------" + "handleMyEventRollback监听方法结束执行" + "------");
    }

    /**
     * 无论事物成功提交还是失败回滚，该方法都会执行
     * @param myEvent
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    @Async
    public void handleMyEventCompletion(MyEvent myEvent) {
        System.out.println("------" + "handleMyEventCompletion监听方法开始执行" + "------");
        System.out.println("handleMyEventCompletion监听myEvent的内容为：" + myEvent);
        System.out.println("------" + "handleMyEventCompletion监听方法结束执行" + "------");
    }
}

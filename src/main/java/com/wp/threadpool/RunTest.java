package com.wp.threadpool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Classname RunTest
 * @Description 向线程池提交任务
 * @Date 2021/1/26 16:29
 * @Created by wangpeng116
 */
public class RunTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /**
         * submit()
         */
        ExecutorService threadPool = Executors.newCachedThreadPool();
        Future<Integer> future = threadPool.submit(() -> {
            Thread.sleep(1000L * 10);
            return 2 * 5;
        });
        //阻塞方法，直到任务有返回才继续执行下面的代码
        Integer num = future.get();
        System.out.println("执行结果：" + num);
        /**
         * execute()
         */
        threadPool.execute(() -> {
            Integer result = 2 * 5;
            System.out.println("执行结果" + result);
        });
    }
}

package com.wp.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.SynchronousQueue;

/**
 * @Classname QueueTest
 * @Description 线程池提供的阻塞队列
 * @Date 2021/1/26 16:00
 * @Created by wangpeng116
 */
public class QueueTest {
    public static void main(String[] args) {
        ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(10);
        LinkedBlockingDeque linkedBlockingDeque = new LinkedBlockingDeque(10);
        //同步移交队列：没有存储元素的能力，依托于一个队列插入元素，一个队列删除元素
        SynchronousQueue synchronousQueue = new SynchronousQueue();
        //插入元素的队列
        new Thread(() -> {
            try {
                synchronousQueue.put(1);
                System.out.println("插入成功");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        //删除元素的队列
        new Thread(() -> {
            try {
                synchronousQueue.take();
                System.out.println("删除成功");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}

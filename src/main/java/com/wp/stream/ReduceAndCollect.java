package com.wp.stream;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Classname ReduceAndCollect
 * @Description 规约操作
 * @Date 2021/1/18 18:44
 * @Created by wangpeng116
 */
public class ReduceAndCollect {
    public static void main(String[] args) {
        @Data
        @AllArgsConstructor
        class Order {
            private Integer id;
            private Integer productCount;
            private Double totalAmount;
        }
        List<Order> list = Lists.newArrayList();
        list.add(new Order(1, 2, 25.12));
        list.add(new Order(2, 5, 257.23));
        list.add(new Order(3, 3, 23332.12));

        Order order = list.parallelStream().reduce(
                //参数1：初始化值
                new Order(0, 0, 0.0)
                //参数2：Stream中两个元素的计算逻辑
                , (Order obj1, Order obj2) -> {
                    System.out.println("执行计算逻辑方法");
                    int productCount = obj1.getProductCount() + obj2.getProductCount();
                    double totalAmount = obj1.getTotalAmount() + obj2.getTotalAmount();
                    return new Order(0, productCount, totalAmount);
                }
                //参数3：并行计算时，对多个并行计算的结果进行合并。也就是说这里的obj1和obj2就已经是上面参数2中计算的结果了
                , (Order obj1, Order obj2) -> {
                    System.out.println("执行合并方法");
                    int productCount = obj1.getProductCount() + obj2.getProductCount();
                    double totalAmount = obj1.getTotalAmount() + obj2.getTotalAmount();
                    return new Order(0, productCount, totalAmount);
                }
        );
        System.out.println(order);
    }
}

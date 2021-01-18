package com.wp.stream;

import com.google.common.collect.Lists;
import com.wp.lambda.cart.CartService;
import com.wp.lambda.cart.Sku;

import java.util.List;

/**
 * @Classname StreamOperator
 * @Description 流的各种操作
 * @Date 2021/1/18 15:54
 * @Created by wangpeng116
 */
public class StreamOperator {
    public static void main(String[] args) {
        List<Sku> list = CartService.getCartSkuList();
        filterTest(list);
    }

    private static void filterTest(List<Sku> list) {
        String a = "f43个g5发4r单位grg";
        System.out.println(Lists.newArrayList(a.split("")));
    }
}

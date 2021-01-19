package com.wp.stream;

import com.wp.lambda.cart.CartService;
import com.wp.lambda.cart.Sku;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Classname StreamVs
 * @Description 对比：原始集合操作和Stream集合操作
 * @Date 2021/1/18 15:18
 * @Created by wangpeng116
 */
public class StreamVs {
    public static void main(String[] args) {
        newCartHandle();
    }

    /**
     * 1、想看看购物车中都有什么商品
     * 2、图书类商品都给买
     * 3、其余的商品中买两件最贵的
     * 4、只需要两件商品的名称和总价
     */


    public static void newCartHandle() {
        //1、尽量少用peek，用map处理即可
        List<Sku> result = CartService.getCartSkuList().stream().peek(sku -> System.out.println(sku)).collect(Collectors.toList());
        Map<Integer, List<Sku>> response = Optional.ofNullable(result).orElseGet(ArrayList::new)
                .stream().collect(Collectors.groupingBy(Sku::getSkuId));
        result = result.stream().sorted(Comparator.comparing(Sku::getTotalNum).thenComparing(Sku::getTotalPrice, Comparator.reverseOrder())).collect(Collectors.toList());
        System.out.println(result);
    }
}

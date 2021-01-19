package com.wp.guava;

import java.util.Optional;

/**
 * @Classname OptionalTest
 * @Description Optional使用
 * @Date 2021/1/19 16:32
 * @Created by wangpeng116
 */
public class OptionalTest {
    public static void main(String[] args) {
        /**
         * 三种创建Optional对象的方式
         */
        // 创建空的Optional对象，即value是null
        Optional empty = Optional.empty();
        //非null值创建
        Optional.of("wp");
        //可以为null
        Optional optional = Optional.ofNullable("wp&&yml");

        /**
         * 判断是否引用缺失
         */
        // 不建议使用
        boolean present = optional.isPresent();
        // 建议使用：当引用存在时执行
        optional.ifPresent(System.out::println);

        /**
         * 当Optional引用缺失时执行，即如果value为null就会执行orElse、orElseGet、orElseThrow方法，其中orElse其实无论何时都会执行，所以推荐使用orElseGet方法
         */
        optional.orElse("lalalala");
        optional.orElseGet(() -> {
            // 自定义引用缺失时的返回值
            return "自定义引用的缺失";
        });
        // 当value为空的时候，就会跑错
        optional = Optional.ofNullable(null);
        try {
            optional.orElseThrow(() -> {
                return new RuntimeException("获取Optional.value异常");
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}

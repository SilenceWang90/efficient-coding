package com.wp.autowired_test;

import org.springframework.stereotype.Component;

/**
 * @author wangpeng
 * @description BeanServant
 * @date 2024/8/21 10:18
 **/
@Component
public class BeanServant {
    public void say(){
        System.out.println("我是BeanServant");
    }
}

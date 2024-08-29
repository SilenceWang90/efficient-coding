package com.wp.autowired_test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wangpeng
 * @description BeanOwner
 * @date 2024/8/21 10:17
 **/
@Component
public class BeanOwner {

    @Autowired
    public void setBean(BeanServant beanServant){
        beanServant.say();
    }
    @Autowired
    public void test(){
        System.out.println("不需要依赖注入的时候我也会被执行");
    }
}

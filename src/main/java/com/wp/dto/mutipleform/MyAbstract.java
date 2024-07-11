package com.wp.dto.mutipleform;

/**
 * @author wangpeng
 * @description MyAbstract
 * @date 2024/7/8 13:34
 **/
public abstract class MyAbstract {

    public String say(String wording) {
        return "MyAbstract再说：" + wording;
    }

    protected void testMe(){
        System.out.println("我是：testMe");
    };
}

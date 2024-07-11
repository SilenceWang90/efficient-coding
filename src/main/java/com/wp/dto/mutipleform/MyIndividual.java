package com.wp.dto.mutipleform;

/**
 * @author wangpeng
 * @description MyIndividual
 * @date 2024/7/8 13:31
 **/
public class MyIndividual extends MyAbstract implements MytestInterface{

    public String say(String wording) {
        return "MyIndividual再说：" + wording;
    }

    @Override
    public void read() {
        System.out.println("不想read");
    }

}

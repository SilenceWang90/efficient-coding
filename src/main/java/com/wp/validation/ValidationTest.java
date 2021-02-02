package com.wp.validation;

import com.google.common.collect.Lists;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @Classname ValidationTest
 * @Description 测试主体
 * @Date 2021/2/2 18:32
 * @Created by wangpeng116
 */
public class ValidationTest {
    //验证器对象
    private static Validator validator;
    //待验证对象
    private static UserInfo userInfo;
    //验证结果结合
    private static Set<ConstraintViolation<UserInfo>> set;


    public static void main(String[] args) {
        //1、初始化验证器
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        //2、初始化待验证对象
        userInfo = new UserInfo();
        userInfo.setUserId("1");
        userInfo.setUserName("wangpeng");
        userInfo.setPassword("123321");
        userInfo.setAge(31);
        //级联验证对象
        UserInfo friend = new UserInfo();
        friend.setAge(71);
        userInfo.setFriends(Lists.newArrayList(friend));
        //3、验证对象
        set = validator.validate(userInfo, UserInfo.Group.class);
        //4、输出验证结果
        set.forEach(item -> {
            System.out.println(item.getMessage());
        });
    }
}

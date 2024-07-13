package com.wp.threadlocal;

import lombok.Data;

/**
 * 创建ThreadLocal对象的操作类，并初始化一个ThreadLocal类型的对象
 *
 * @author wangpeng
 * @description ThreadLocalHolder
 * @date 2024/7/13 10:50
 **/
public class ThreadLocalHolder {
    /***
     * 创建ThreadLocal对象
     */
    private static final ThreadLocal<UserInfo> USER_INFO = ThreadLocal.withInitial(()->{
        UserInfo userInfo = new UserInfo();
        userInfo.setAge(1);
        userInfo.setName("wzl");
        return userInfo;
    });

    /**
     * 线程设置userInfo
     *
     * @param userInfo
     */
    public static void setUserInfo(UserInfo userInfo) {
        USER_INFO.set(userInfo);
    }

    /**
     * 线程获取userInfo
     *
     * @return userInfo
     */
    public static UserInfo getUserInfo() {
        return USER_INFO.get();
    }

    @Data
    public static class UserInfo {
        private String name;
        private Integer age;
    }

}
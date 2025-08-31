package com.wp.protobuf;

import com.google.common.collect.Lists;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.Arrays;
import java.util.List;

/**
 * @author wangpeng
 * @description 测试ProtoBuf的序列化和反序列化的功能是否生效
 * @date 2025/8/31 17:30
 **/
public class TestProtoBuf {
    public static void main(String[] args) {
        byte[] bytes = serial();
        UserModule.User user = deserial(bytes);
        user.getUserId();
        user.getAge();
        user.getUserName();
        user.getFavouriteList();
        System.out.println(user);
    }

    public static byte[] serial() {
        List<String> favourates = Lists.newArrayList("跑步", "电影");
        UserModule.User user = UserModule
                .User
                .newBuilder()
                .setUserId("1").setAge(18).setUserName("wangpeng").addAllFavourite(favourates)
                .build();
        byte[] userBytes = user.toByteArray();
        System.out.println(Arrays.toString(userBytes));
        return userBytes;
    }

    public static UserModule.User deserial(byte[] data) {
        try {
            return UserModule.User.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }
}

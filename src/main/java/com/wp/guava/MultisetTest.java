package com.wp.guava;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.primitives.Chars;

/**
 * @Classname MultisetTest
 * @Description 实战
 * @Date 2021/1/19 18:19
 * @Created by wangpeng116
 */
public class MultisetTest {
    private static final String text = "32r4tg54yu654r43t45u7678dw234f4g";

    public static void main(String[] args) {
        //创建Multiset集合
        Multiset<Character> multiset = HashMultiset.create();
        char[] chars = text.toCharArray();
        //将字符串拆成字符放置到Multiset集合中
        Chars.asList(chars).stream().forEach(charItem -> {
            multiset.add(charItem);
        });
        //字符串长度
        System.out.println("size：" + multiset.size());
        //字符3出现的次数
        System.out.println("count：" + multiset.count('3'));
    }
}

package com.wp.guava;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

/**
 * @Classname SetsTest
 * @Description Set工具类
 * @Date 2021/1/20 15:18
 * @Created by wangpeng116
 */
public class SetsTest {
    private static final Set<Integer> set1 = Sets.newHashSet(1, 2, 3);
    private static final Set<Integer> set2 = Sets.newHashSet(4, 5, 6);

    public static void main(String[] args) throws JsonProcessingException {
        // 并集
        Set<Integer> setTest1 = Sets.union(set1, set2);
        System.out.println(setTest1);
        // 交接
        Set<Integer> setTest2 = Sets.intersection(set1, set2);
        System.out.println(setTest2);
        // 差集(参数1有而参数2没有)
        Set<Integer> setTest3 = Sets.difference(set1, set2);
        Set<Integer> setTest4 = Sets.difference(set2, set1);
        System.out.println(setTest3);
        System.out.println(setTest4);
        // 相对差集(A,B非交集部分的元素，即属于A但不属于B以及属于B但不属于A的元素)
        Set<Integer> setTest5 = Sets.symmetricDifference(set1, set2);
        System.out.println(setTest5);
        // 拆解成各种组合的子集
        ObjectMapper objectMapper = new ObjectMapper();
        Set<Set<Integer>> setTest6 = Sets.powerSet(set1);
        System.out.println(objectMapper.writeValueAsString(setTest6));
        // 笛卡尔积
        Set<List<Integer>> set7 = Sets.cartesianProduct(set1,set2);
        System.out.println(set7);

        // 集合拆解
        List<Integer> list = Lists.newArrayList(1,2,3,4,5,6,7);
        // 3个3个一组
        List<List<Integer>> collection = Lists.partition(list,3);
        System.out.println("集合拆解："+collection);
    }
}

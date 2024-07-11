package com.wp.dto.mutipleform;

/**
 * @author wangpeng
 * @description MainTest
 * @date 2024/7/8 13:26
 **/
public class MainTest {
    public static void main(String[] args) {
        MytestInterface myIndividual = new MyIndividual();
        System.out.println(myIndividual.getClass() == MytestInterface.class);
        System.out.println(myIndividual.getClass() == MyIndividual.class);
        System.out.println(myIndividual.say("heelo"));
        // 即使强制类型转换，运行时多态还是执行实现类(子类)的方法，强制类型转换无效
        System.out.println(((MyAbstract) myIndividual).say("heelo"));
        /*MyAbstract a = new MyIndividual();
        a.testMe();
        MyAbstract b = (MyAbstract)myIndividual;
        b.testMe();*/

        /*List<Map<String, Object>> list1 = Lists.newArrayList();
        List<Map<String, Object>> list2 = Lists.newArrayList();
        Map<String, Object> map1 = Maps.newHashMap();
        map1.put("a", "aaa");
        Map<String, Object> map2 = Maps.newHashMap();
        map2.put("b", "bbb");
        Map<String, Object> map3 = Maps.newHashMap();
        map3.put("c", "ccc");
        list2.add(map1);
        list2.add(map2);
        list2.add(map3);
        list1 = list2;
        list1.forEach(obj -> obj.keySet().forEach(key -> {
            System.out.println("key是：" + key);
            System.out.println("value是：" + obj.get(key));
        }));*/
    }
}

package com.wp;

import com.wp.util.SnowflakeIdUtil;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * 雪花ID生成工具测试类
 */
public class SnowflakeIdUtilTest {

    @Test
    public void testGenerateId() {
        // 测试默认实例生成ID
        System.out.println("使用默认实例生成ID:");
        for (int i = 0; i < 10; i++) {
            long id = SnowflakeIdUtil.generateId();
            System.out.println("ID " + (i + 1) + ": " + id);
        }

        // 测试自定义机器ID
        System.out.println("\n使用自定义机器ID生成ID:");
        SnowflakeIdUtil customIdGenerator = new SnowflakeIdUtil(123);
        for (int i = 0; i < 10; i++) {
            long id = customIdGenerator.nextId();
            System.out.println("ID " + (i + 1) + ": " + id);
        }

        // 测试ID唯一性
        System.out.println("\n测试ID唯一性:");
        Set<Long> idSet = new HashSet<>();
        int testCount = 10000;
        for (int i = 0; i < testCount; i++) {
            long id = SnowflakeIdUtil.generateId();
            idSet.add(id);
        }
        System.out.println("生成了 " + testCount + " 个ID");
        System.out.println("去重后有 " + idSet.size() + " 个ID");
        System.out.println("ID是否唯一: " + (idSet.size() == testCount));
    }
}
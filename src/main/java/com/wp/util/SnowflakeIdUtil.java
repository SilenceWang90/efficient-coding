package com.wp.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 雪花ID生成工具类
 * 基于Twitter的Snowflake算法
 */
public class SnowflakeIdUtil {

    // 起始时间戳 (2020-01-01 00:00:00)
    private static final long START_TIMESTAMP = 1577808000000L;

    // 机器ID位数
    private static final long MACHINE_ID_BITS = 10L;

    // 序列号位数
    private static final long SEQUENCE_BITS = 12L;

    // 机器ID最大值
    private static final long MAX_MACHINE_ID = (1L << MACHINE_ID_BITS) - 1;

    // 序列号最大值
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;

    // 机器ID左移位数
    private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;

    // 时间戳左移位数
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS;

    // 机器ID
    private final long machineId;

    // 序列号
    private final AtomicLong sequence = new AtomicLong(0L);

    // 上一次生成ID的时间戳
    private long lastTimestamp = -1L;

    /**
     * 构造方法
     * @param machineId 机器ID (0-1023)
     */
    public SnowflakeIdUtil(long machineId) {
        if (machineId < 0 || machineId > MAX_MACHINE_ID) {
            throw new IllegalArgumentException("Machine ID must be between 0 and " + MAX_MACHINE_ID);
        }
        this.machineId = machineId;
    }

    /**
     * 生成雪花ID
     * @return 雪花ID
     */
    public synchronized long nextId() {
        long currentTimestamp = System.currentTimeMillis();

        // 检查时间戳是否回拨
        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id.");
        }

        // 如果是同一时间戳，则增加序列号
        if (currentTimestamp == lastTimestamp) {
            long currentSequence = sequence.incrementAndGet();
            // 如果序列号溢出，则等待到下一个时间戳
            if (currentSequence > MAX_SEQUENCE) {
                currentTimestamp = getNextTimestamp(lastTimestamp);
                sequence.set(0L);
            }
        } else {
            // 不同时间戳，重置序列号
            sequence.set(0L);
        }

        lastTimestamp = currentTimestamp;

        // 组合雪花ID
        return ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_SHIFT) |
               (machineId << MACHINE_ID_SHIFT) |
               sequence.get();
    }

    /**
     * 获取下一个时间戳
     * @param lastTimestamp 上一次的时间戳
     * @return 下一个时间戳
     */
    private long getNextTimestamp(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    /**
     * 静态内部类，用于实现单例模式
     */
    public static class Singleton {
        private static final SnowflakeIdUtil INSTANCE = new SnowflakeIdUtil(0);

        private Singleton() {}

        public static SnowflakeIdUtil getInstance() {
            return INSTANCE;
        }
    }

    /**
     * 获取默认实例
     * @return 雪花ID生成器实例
     */
    public static SnowflakeIdUtil getInstance() {
        return Singleton.getInstance();
    }

    /**
     * 生成雪花ID（使用默认实例）
     * @return 雪花ID
     */
    public static long generateId() {
        return getInstance().nextId();
    }
}
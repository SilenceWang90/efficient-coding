package com.wp.controller;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author wangpeng
 * @description RedisController
 * @date 2026/2/12 09:25
 **/
@RestController
@RequestMapping("/redis")
@Slf4j
public class RedisController {
    /**
     * 锁id
     */
    private static final String lockKey = "redislockid";
    private String lockValue;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 上锁
     */
    @RequestMapping("/redisLock")
    public void redisLock() {
        boolean isLocked = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, Thread.currentThread().getName(), Duration.ofSeconds(60));
        this.lockValue = Thread.currentThread().getName();
        log.info("lockKey锁的value值为{}", Thread.currentThread().getName());
        if (isLocked) {
            try {
                // 业务逻辑处理
            } finally {
                // 删除锁
                stringRedisTemplate.delete(lockKey);
            }
        }
        // 删除锁逻辑放在外面可以么？为什么？
        /*finally {
            stringRedisTemplate.delete(lockId);
        }*/
    }

    /**
     * 解锁
     */
    @RequestMapping("/redisUnLock")
    public void redisUnLock() {
        List<String> keyList = Lists.newArrayList(lockKey);
        this.releaseLock(keyList, lockValue);
    }

    /**
     * 锁释放：基于lua脚本原子性执行，安全释放。values数组中最好都是string，lua脚本执行默认按照string读取。若要其他类型需要在RedisScrpit脚本中变换类型。
     *
     * @param keys   脚本中keys[]的内容，索引从1开始。
     * @param values 脚本中ARGV[]的内容，索引从1开始。一般只需要传分布式锁存储的值
     * @return true释放成功，false释放失败
     */
    private boolean releaseLock(List<String> keys, String... values) {
        // 1、创建脚本
        String script = "if redis.call(\"GET\",KEYS[1]) == ARGV[1] then\n" +
                "\treturn redis.call(\"DEL\",KEYS[1])\n" +
                "else\n" +
                "\treturn 0\n" +
                "end";
        // 2、封装脚本以及脚本返回值类型
        RedisScript<Long> redisScript = RedisScript.of(script, Long.class);
        // 3、执行脚本。返回1则意味着DEL删除命令删除了一个key。
        Long result = stringRedisTemplate.execute(redisScript, keys, values);
        return result != null && result == 1L;
    }


    /**
     * 锁续约
     */
    @RequestMapping("/redisRenewLock")
    public void redisRenewLock() {
        // 锁的key
        List<String> keyList = Lists.newArrayList(lockKey);
        // 锁的value值；锁的续约时间(单位毫秒)
        this.renewLock(keyList, lockValue, "60000");
    }

    /**
     * 锁续约：基于lua脚本原子性执行，安全续约。values参数必须都是string，lua脚本执行默认按照string读取。非要其他类型需要在RedisScrpit脚本中变换类型。
     *
     * @param keys   脚本中keys[]的内容，索引从1开始。
     * @param values 脚本中ARGV[]的内容，索引从1开始。此方法需要2个参数，分布式锁存储的值以及续约的时间(values[0] = 锁的 Value (所有权标识), values[1] = 续约时长(毫秒))。PEXPIRE方法续约时间单位默认是毫秒，EXPIRE方法默认时间单位是秒，根据需要选择任一方法放在脚本中即可
     * @return true续约成功，false续约失败
     */
    public boolean renewLock(List<String> keys, String... values) {
        // 1、创建脚本
        String script = "if redis.call(\"GET\",KEYS[1]) == ARGV[1] then\n" +
                "\treturn redis.call(\"PEXPIRE\",KEYS[1],ARGV[2])\n" +
                "else\n" +
                "\treturn 0\n" +
                "end";
        // 2、封装脚本以及脚本返回值类型
        RedisScript<Long> redisScript = RedisScript.of(script, Long.class);
        // 3、执行脚本。返回1则意味着PEXPIRE续期命令当前key的续约成功
        Long result = stringRedisTemplate.execute(redisScript, keys, values);
        return result != null && result == 1L;
    }
}

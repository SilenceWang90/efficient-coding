package com.wp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.rabbitmq.client.Channel;
import com.wp.dto.MyTestMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 上锁
     */
    @RequestMapping("/redisLock")
    public String redisLock() throws JsonProcessingException {
        String result;
        boolean isLocked = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, Thread.currentThread().getName(), Duration.ofSeconds(60));
        this.lockValue = Thread.currentThread().getName();
        log.info("lockKey锁的value值为{}", Thread.currentThread().getName());
        if (isLocked) {
            /** 发送延迟消息 **/
            // 消息元信息
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setExpiration("5000");
            // 消息数据
            MyTestMessage myTestMessage = new MyTestMessage();
            myTestMessage.setName("wangpeng").setAge(18).setPrice(BigDecimal.TEN).setCreateDateTime(LocalDateTime.now());
            rabbitTemplate.convertAndSend(objectMapper.writeValueAsString(myTestMessage), messageProperties);
            result = "上锁成功";
            try {
                // 业务逻辑处理
            } finally {
                /** 问题2：删除锁。这种写法就安全了嘛？ **/
                // 不行。加入A线程上锁成功，但是因为下游服务响应过慢导致锁自动释放。此时B线程获取锁成功但是还未执行完成，A线程的下游服务在此时响应结果，A线程开始释放锁。
//                stringRedisTemplate.delete(lockKey);
            }
        } else {
            result = "上锁失败";
        }
        /** 问题1：删除锁逻辑放在外面可以么？为什么？**/
        // 不行。没有获取锁就释放锁，会释放其他线程的锁
        /*finally {
            stringRedisTemplate.delete(lockId);
        }*/
        return result;
    }

    @RabbitListener(queues = "wp-redis-queue", containerFactory = "rabbitListenerContainerFactory")
    @RabbitHandler
    public void redisDeplayQueueListener(Message message, Channel channel) throws IOException {
        log.info("获取message的信息为：{}", message);
        log.info("获取message的信息为：{}", objectMapper.readValue(new String(message.getBody()), MyTestMessage.class));
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 解锁
     */
    @RequestMapping("/redisUnLock")
    public boolean redisUnLock() {
        List<String> keyList = Lists.newArrayList(lockKey);
        return this.releaseLock(keyList, lockValue);

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
    public boolean redisRenewLock() {
        // 锁的key
        List<String> keyList = Lists.newArrayList(lockKey);
        // 锁的value值；锁的续约时间(单位毫秒)
        return this.renewLock(keyList, lockValue, "60000");
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

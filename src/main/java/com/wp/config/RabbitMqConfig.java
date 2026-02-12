package com.wp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wp.dto.MyTestMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author wangpeng
 * @description rabbitMq配置类；包含生产者和消费者
 * @date 2026/2/12 11:32
 **/
@Configuration
@EnableRabbit
@Slf4j
public class RabbitMqConfig {
    /**
     * 连接工厂配置，生产者消费者连接MQ的基础配置
     *
     * @return
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost", 5672);
        /** 集群连接方式：不需要配置host和port，如下配置address即可。集群多个地址逗号分隔，port端口默认是5672
         CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
         connectionFactory.setAddresses(ip1:port1,ip2:port2,ip3:port3);**/
        connectionFactory.setUsername("wangpeng");
        connectionFactory.setPassword("Wangpeng90");
        connectionFactory.setVirtualHost("/");
        // 是否启用发送确认机制
        // 是否启用发送到交换区的确认
        connectionFactory.setPublisherConfirms(true);
        // 是否启用发送到队列的确认
        connectionFactory.setPublisherReturns(true);
        return connectionFactory;
    }

    /**
     * 生产者：rabbitTemplate配置，用于生产者发送消息。
     * 消息转换器不配置也可以，直接在代码里将消息体转为字符串即可
     *
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        //配置消息转换器，如果是发送对象则会自动转成json格式便于在控制台查看
        template.setMessageConverter(new MessageConverter() {
            //发送消息，把消息转换成json数据，convertAndSend方法会调用此方法
            @Override
            public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
                Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
                return jackson2JsonMessageConverter.toMessage(o, messageProperties);
            }

            //接收消息，把消息从json转换成Object
            @Override
            public Object fromMessage(Message message) throws MessageConversionException {
                Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
                return jackson2JsonMessageConverter.fromMessage(message);
            }
        });
        SendMessageCallBack sendMessageCallBack = new SendMessageCallBack();
        //发送交换区确认机制使用的策略。基于connectionFactory.setPublisherConfirms(true);的配置才能生效
        template.setConfirmCallback(sendMessageCallBack);
        //发送到队列确认机制使用的策略。基于connectionFactory.setPublisherReturns(true);的配置才能生效
        template.setMandatory(true);
        template.setReturnCallback(sendMessageCallBack);
        return template;
    }

    /**
     * 消费者：监听方式接收数据配置
     * 消息转换器不配置也可以，收发消息时直接将消息体转为字符串即可
     *
     * @return
     */
    @Bean("simpleRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //设置该监听工厂创建消费者的个数，创建几个就会在mq中展示几个。一般配置为1
        factory.setConcurrentConsumers(1);
        // 根据MQ服务器的CPU情况设定。
        factory.setMaxConcurrentConsumers(10);
        factory.setPrefetchCount(1);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setMessageConverter(new MessageConverter() {
            //发送消息，把消息转换成json数据
            @Override
            public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
                Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
                return jackson2JsonMessageConverter.toMessage(o, messageProperties);
            }

            //接收消息，把消息从json转换成Object
            @Override
            public Object fromMessage(Message message) throws MessageConversionException {
                Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
                return jackson2JsonMessageConverter.fromMessage(message);
            }
        });
        return factory;
    }

    @Component
    public static class SendMessageCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback{
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            log.info("ConfirmCallBack start");
            //消息id，发送的消息id
            log.info("correlationData：{}", correlationData);
            //broker是否接收到了，收到则返回true，未收到则返回false
            log.info("broker是否接收到了：{}", ack);
            //exchange接收失败返回信息错误信息，如果成功了则该信息为null
            log.info("broker接收失败返回信息错误信息：{}", cause);
            log.info("ConfirmCallBack end");
        }

        @Override
        public void returnedMessage(Message message, int i, String s, String s1, String s2) {
            log.info("ReturnCallback start");
            //消息信息
            ObjectMapper objectMapper = new ObjectMapper();
            MyTestMessage myMessage = new MyTestMessage();
            try {
                myMessage = objectMapper.readValue(message.getBody(), MyTestMessage.class);
            } catch (IOException e) {
                log.error("消息体转换异常：{}", e);
            }
            log.info("发送的消息为：{}", myMessage);
            //消息响应码
            log.info("第二个参数i：{}",i);
            //发送失败时返回NO_ROUTE字符串，报错信息，没有找到队列之类的
            log.info("报错信息：{}",s);
            //exchange的名称
            log.info("exchange的名称：{}",s1);
            //routing_key名称
            log.info("routing_key名称：{}",s2);
            log.info("ReturnCallback end");
        }
    }

}

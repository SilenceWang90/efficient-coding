package com.wp.config.customhttpmessageconverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.Date;
import java.util.List;

/**
 * @Classname CustomHttpMessageConverterConfigure
 * @Description 自定义消息转换器配置类
 * @Date 2021/5/18 11:06
 * @Created by wangpeng116
 */
@Configuration
public class CustomHttpMessageConverterConfigure extends WebMvcConfigurationSupport {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new ResourceHttpMessageConverter());
        converters.add(new AllEncompassingFormHttpMessageConverter());
        converters.add(new StringHttpMessageConverter());
        // 1、替换MappingJackson2HttpMessageConverter
        converters.add(new CustomHttpMessageConverter());
        // 2、重写MappingJackson2HttpMessageConverter中ObjectMapper序列化的内容
//        converters.add(jackson2HttpMessageConverter());
    }

    /**
     * 1、序列化时间戳解决Long类型精度丢失的问题
     * 2、日期序列化、反序列化处理
     *
     * @return 值
     */
//    @Bean
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        // Long类型转String类型（解决浏览器Long类型精度丢失问题）
        SimpleModule longSimpleModule = new SimpleModule();
        longSimpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        mapper.registerModule(longSimpleModule);
        // Date类型转换
        SimpleModule dateSimpleModule = new SimpleModule();
        dateSimpleModule.addSerializer(Date.class, new DateJsonSerializer());
        dateSimpleModule.addDeserializer(Date.class, new DateJsonDeserializer());
        mapper.registerModule(dateSimpleModule);
        converter.setObjectMapper(mapper);
        return converter;
    }

}

package com.wp.config.customhttpmessageconverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Classname CustomHttpMessageConverter
 * @Description 自定义消息转换器（自定义Jackson消息转换器，所有实现均用父类的，只是增加原始消息的读取时的输出）
 * @Date 2021/5/18 10:18
 * @Created by wangpeng116
 */
@Configuration
@Slf4j
public class CustomHttpMessageConverter extends AbstractJackson2HttpMessageConverter {

    protected CustomHttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected boolean supports(Class aClass) {
        return true;
    }

    @Override
    protected Object readInternal(Class aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(httpInputMessage.getBody()));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        String str = sb.toString();
        log.info("CustomHttpMessageConverter消息转换器要处理的传入信息为：{}", str);
        return super.readInternal(aClass, httpInputMessage);
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        super.writeInternal(o, httpOutputMessage);
    }
}

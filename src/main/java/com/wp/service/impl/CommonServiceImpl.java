package com.wp.service.impl;

import com.wp.dto.TestXmlChildDto;
import com.wp.dto.TestXmlDto;
import com.wp.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * @Classname CommonServiceImpl
 * @Description 通用服务类
 * @Date 2021/7/20 14:22
 * @Created by wangpeng116
 */
@Slf4j
@Service
public class CommonServiceImpl implements CommonService {

    public static void main(String[] args) throws JAXBException {
        /** 1、生成要转换xml数据 **/
        TestXmlDto testXmlDto = new TestXmlDto();
        testXmlDto.setAge(12L);
        testXmlDto.setName("YML");
        TestXmlChildDto testXmlChildDto = new TestXmlChildDto();
        testXmlChildDto.setAddress("地址");
        testXmlChildDto.setDescription("描述");
        testXmlDto.setTestXmlChildDto(testXmlChildDto);
        /** 2、生成转换工具 **/
        JAXBContext jaxbContext = JAXBContext.newInstance(TestXmlDto.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        // 不输出<xml>标签的内容
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        // 格式化输出内容，不设置此值是最终是一行输出
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        // 防止文件中文乱码
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        /** 3、获取转换结果 **/
        StringWriter writer = new StringWriter();
        marshaller.marshal(testXmlDto, writer);
        String result = writer.toString();
        log.info("输出转换结果：{}", result);
    }
}

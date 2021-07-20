package com.wp.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @Classname TestXmlDto
 * @Description 生成XML数据的DTO
 * @Date 2021/7/20 14:31
 * @Created by wangpeng116
 */
@Data
@XmlRootElement
public class TestXmlDto {
    private String name;
    private Long age;
    private TestXmlChildDto testXmlChildDto;
}

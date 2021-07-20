package com.wp.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @Classname TestXmlChildDto
 * @Description 子类
 * @Date 2021/7/20 14:36
 * @Created by wangpeng116
 */
@Data
@XmlRootElement
public class TestXmlChildDto {
    private String address;
    private String description;
}

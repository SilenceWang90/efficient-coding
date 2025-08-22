package com.wp.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangpeng
 * @description TestDto
 * @date 2025/8/18 08:38
 **/
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class TestDto {
    private String name;
    private int age;
}

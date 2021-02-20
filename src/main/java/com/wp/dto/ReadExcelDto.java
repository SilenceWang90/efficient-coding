package com.wp.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.wp.util.LocalDateTimeStringConverter;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Classname ReadExcelDto
 * @Description 读取Excel实体
 * @Date 2021/2/20 9:58
 * @Created by wangpeng116
 */
@Data
public class ReadExcelDto {
    /**
     * 1、通过ExcelProperty来确认对应哪一列，可以是列索引(从0开始)，可以是列名
     * 2、官方不建议index和value一起用，2个配置属性同时最好只用一个
     */
    @ExcelProperty(value = "userId")
    private Long userId;
    @ExcelProperty(index = 1)
    private String userName;
    /**
     * 时间类型转换器协助数据处理，否则读取时间格式数据的时候会报错，EasyExcel无法直接将日期格式转换成LocalDateTime
     */
    @ExcelProperty(index = 2, converter = LocalDateTimeStringConverter.class)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime datetime;
}

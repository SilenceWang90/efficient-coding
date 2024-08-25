package com.wp.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.wp.util.LocalDateTimeStringConverter;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Classname UserExportDto
 * @Description 数据导出实体对象
 * @Date 2021/2/19 15:43
 * @Created by wangpeng116
 */
@Data
public class UserExportDto {
    /**
     * @ExcelProperty：
     * (1) value定义对应列的名称，即excel的首行标题
     * (2) 属性的先后顺序就是对应列的先后顺序
     */

    @ExcelProperty(value = "用户id")
    private Long userId;

    @ExcelProperty(value = "用户名称")
    private String userName;

    @ExcelProperty(value = "用户年龄")
    private Integer age;

    // 非基本类型无法导出，需要通过转换器进行转换才可以导出
    @ExcelProperty(value = "创建时间", converter = LocalDateTimeStringConverter.class)
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒SSS毫秒")
    private LocalDateTime createTime;
}

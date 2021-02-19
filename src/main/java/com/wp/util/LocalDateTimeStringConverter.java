package com.wp.util;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Classname LocalDateTimeStringConverter
 * @Description 转换器：LocalDateTime转String
 * @Date 2021/2/19 16:17
 * @Created by wangpeng116
 */
public class LocalDateTimeStringConverter implements Converter<LocalDateTime> {
    /**
     * 什么类型要转换
     *
     * @return
     */
    @Override
    public Class supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    /**
     * 转换成什么类型
     *
     * @return
     */
    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * 导入时转换使用
     *
     * @param cellData
     * @param excelContentProperty
     * @param globalConfiguration
     * @return
     * @throws Exception
     */
    @Override
    public LocalDateTime convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty
            , GlobalConfiguration globalConfiguration) throws Exception {
        return null;
    }

    /**
     * 导出时转换使用
     *
     * @param localDateTime
     * @param excelContentProperty
     * @param globalConfiguration
     * @return
     * @throws Exception
     */
    @Override
    public CellData convertToExcelData(LocalDateTime localDateTime, ExcelContentProperty excelContentProperty
            , GlobalConfiguration globalConfiguration) throws Exception {
        // 有时间类型基本格式就直接引用，如果没有就用默认的ISO。该时间类型就是实体上的@DateTimeFormat，注意这个可不是Spring的注解，是alibaba的注解哦！！！
        if (excelContentProperty == null || excelContentProperty.getDateTimeFormatProperty() == null) {
            return new CellData(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(localDateTime));
        } else {
            return new CellData(DateTimeFormatter.ofPattern(excelContentProperty.getDateTimeFormatProperty().getFormat()).format(localDateTime));
        }
    }
}

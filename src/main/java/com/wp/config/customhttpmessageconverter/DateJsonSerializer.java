package com.wp.config.customhttpmessageconverter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.util.Date;

/**
 * @Classname DateJsonSerializer
 * @Description 日期类型反序列化(Object to json)
 * @Date 2021/5/18 14:22
 * @Created by wangpeng116
 */
public class DateJsonSerializer extends JsonSerializer<Date> {
    private static DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void serialize(Date date, JsonGenerator json, SerializerProvider provider) throws IOException {
        json.writeString(new DateTime(date).toString(dateFormatter));
    }
}

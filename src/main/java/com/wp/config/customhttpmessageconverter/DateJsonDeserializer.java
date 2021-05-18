package com.wp.config.customhttpmessageconverter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.util.Date;

/**
 * @Classname DateJsonDeserializer
 * @Description 日期类型反序列化(json to Object)
 * @Date 2021/5/18 14:23
 * @Created by wangpeng116
 */
public class DateJsonDeserializer extends JsonDeserializer<Date> {
    private static DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    @Override
    public Date deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
        return DateTime.parse(parser.getText(), dateFormatter).toDate();
    }
}

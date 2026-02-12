package com.wp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author wangpeng
 * @description MyTestMessage
 * @date 2026/2/12 11:28
 **/
@Data
@Accessors(chain = true)
public class MyTestMessage {
    private String name;
    private Integer age;
    private BigDecimal price;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createDateTime;
}

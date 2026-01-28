package com.wp.applicationevent;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * @author wangpeng
 * @description MyEvent
 * @date 2026/1/28 17:41
 **/
@Data
public class MyEvent{
    private String name;
    private Integer age;
}

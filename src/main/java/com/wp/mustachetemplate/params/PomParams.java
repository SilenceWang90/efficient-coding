package com.wp.mustachetemplate.params;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author wangpeng
 * @Date 2023/10/24 18:35
 */
@Component
@Data
@ConfigurationProperties(prefix = "pom")
public class PomParams extends BaseConfigParams{
    List<Map<String,String>> dependencies;
}

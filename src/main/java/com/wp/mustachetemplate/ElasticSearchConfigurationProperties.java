package com.wp.mustachetemplate;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author wangpeng
 * @Date 2023/10/18 14:46
 */
@Component
@ConfigurationProperties(prefix = "elastic-search")
@Data
public class ElasticSearchConfigurationProperties {
    private String host;
    private String port;
    private String protocol;
}

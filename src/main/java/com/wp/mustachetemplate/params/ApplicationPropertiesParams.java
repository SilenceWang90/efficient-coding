package com.wp.mustachetemplate.params;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author wangpeng
 * @Date 2023/10/24 18:31
 */
@Component
@Data
@ConfigurationProperties(prefix = "application-properties")
public class ApplicationPropertiesParams extends BaseConfigParams {
    private String elasticHost;
    private String elasticPort;
    private String elasticProtocol;
}

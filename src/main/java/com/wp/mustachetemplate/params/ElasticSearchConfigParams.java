package com.wp.mustachetemplate.params;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description
 * @Author wangpeng
 * @Date 2023/10/24 18:17
 */
@Component
@Data
@ConfigurationProperties(prefix = "elastic-search-config")
public class ElasticSearchConfigParams extends BaseConfigParams {
    // 参数信息，渲染模板
    private List<String> basePackage;

}

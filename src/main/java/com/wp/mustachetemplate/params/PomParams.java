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
    // todo：可优化，增加version版本选择以及plugins等配置内容。创建denpendecy对象即可～version为集合，其他属性等确认后再做添加，和applicaiton.yml同步修改
    List<Map<String,String>> dependencies;
}

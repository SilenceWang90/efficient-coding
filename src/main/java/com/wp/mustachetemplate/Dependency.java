package com.wp.mustachetemplate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Description
 * @Author wangpeng
 * @Date 2023/10/18 11:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Dependency {
    private String groupId;
    private String artifactId;
    private String version;
}

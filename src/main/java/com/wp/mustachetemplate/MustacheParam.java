package com.wp.mustachetemplate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Description
 * @Author wangpeng
 * @Date 2023/10/18 10:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MustacheParam {
    private String basePackage;
    private List<Dependency> dependencies;
    private boolean condition;
}

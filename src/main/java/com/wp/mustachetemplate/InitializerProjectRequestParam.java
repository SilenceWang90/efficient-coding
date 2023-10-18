package com.wp.mustachetemplate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * @Description 生成模板中需要的组件
 * @Author wangpeng
 * @Date 2023/10/18 14:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class InitializerProjectRequestParam {
    // 1、组件类型
    private String componentType;
    // 2、模板名称
    private String mustacheTemplateName;
    // 3、参数信息
    // 3.1、普通类型参数变量
    private Map<String, Object> params;
    // 3.2、集合类型参数变量：目前仅pom渲染时会用到此参数
    private List<Map<String, Object>> collectionParams;
    // 4、文件输出地址
    private String outputUri;
}

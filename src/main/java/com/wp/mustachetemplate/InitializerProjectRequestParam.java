package com.wp.mustachetemplate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
    private Map<String, Object> params;
    // 4、文件所在目录(绝对路径)
    private String directoryPath;
    // 5、文件绝对路径(绝对路径)
    private String filePath;
}

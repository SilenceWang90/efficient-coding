package com.wp.mustachetemplate.params;

import lombok.Data;

/**
 * @Description
 * @Author wangpeng
 * @Date 2023/10/24 18:25
 */
@Data
public class BaseConfigParams {
    // 1、组件类型
    private String componentType;
    // 2、模板名称
    private String mustacheTemplateName;
    // 3、文件所在目录(绝对路径)
    private String directoryPath;
    // 4、文件绝对路径(绝对路径)
    private String filePath;
}

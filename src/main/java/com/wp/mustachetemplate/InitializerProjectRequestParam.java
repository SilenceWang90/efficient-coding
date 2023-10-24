package com.wp.mustachetemplate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

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
    // 待打包文件目录
    private String sourceFolderPath;
    // 模板以及待渲染模板参数
    List<MustacheTemplateParams> templateParams;
}

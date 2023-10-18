package com.wp.mustachetemplate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
    // 组件类型
    private String componentType;
}

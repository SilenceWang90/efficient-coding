package com.wp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Description
 * @Author wangpeng
 * @Date 2023/10/20 11:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BatchDownloadPathRequestParam {
    private String sourceFolderPath;
}

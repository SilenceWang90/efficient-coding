package com.wp.generate_file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangpeng
 * @description 文档生成
 * @date 2024/8/25 10:05
 **/
@RestController
@RequestMapping("/generateFile")
@Slf4j
public class GenerateFileController {
    /**
     * 导出excel
     */
    @RequestMapping("generateExcel")
    public void generateExcel() {
        log.info("参见笔记《EasyExcel倒入、导出、按照模板导出》");
    }
}

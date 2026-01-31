package com.wp.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.easywatermark.core.config.FontConfig;

import java.awt.*;
import java.io.File;

/**
 * @author wangpeng
 * @description 水印中字体配置
 * @date 2026/1/31 15:20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyFontConfig extends FontConfig {
    // 字体颜色
    private Color color = Color.BLACK;
    // 字体文件
    private File fontFile;
    /**
     * 默认的字体，目前仅在图片水印中生效
     * Default font name is Dialog
     */
    private String fontName = "Dialog";
    // 字体大小
    private int fontSize = 12;
    /**
     * 字体样式
     *
     * @see Font#PLAIN 正常
     * @see Font#BOLD 加粗
     * @see Font#ITALIC 斜体
     */
    private int fontStyle = Font.PLAIN;
}

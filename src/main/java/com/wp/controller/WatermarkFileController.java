package com.wp.controller;

import com.wp.config.MyFontConfig;
import com.wp.config.MyWatermarkConfig;
import lombok.extern.slf4j.Slf4j;
import org.easywatermark.core.EasyWatermark;
import org.easywatermark.core.config.FontConfig;
import org.easywatermark.core.config.WatermarkConfig;
import org.easywatermark.enums.EasyWatermarkTypeEnum;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author wangpeng
 * @description WatermarkFileController
 * @date 2026/1/31 15:22
 **/
@RestController
@RequestMapping("/watermarkFile")
@Slf4j
public class WatermarkFileController {
    /**
     * 文件添加水印
     *
     * @param file     文件
     * @param response 响应流
     */
    @RequestMapping("/generateWatermarkFile")
    public void generateWatermarkFile(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        /** 1、创建水印配置信息 **/
        // 1.1、水印基础配置
        WatermarkConfig watermarkConfig = new MyWatermarkConfig();
        // 设置水印透明度
        watermarkConfig.setAlpha(0.5f);
        // 水印倾斜45度
        watermarkConfig.setAngle(45);
        // 1.2、水印文字
        FontConfig fontConfig = new MyFontConfig();
        // 设置水印文字大小
        fontConfig.setFontSize(30);
        // 文字风格
        fontConfig.setFontStyle(Font.PLAIN);
        /** 2、创建水印文件 **/
        byte[] watermarkFile = EasyWatermark.create()
                // 要添加水印的文件
                .file(file.getBytes())
                // 水印配置
                .config(watermarkConfig)
                // 水印字体配置
                .config(fontConfig)
                // 添加的水印是文字
                .text("我自定义的水印")
                // 水印类型(铺满)
                .easyWatermarkType(EasyWatermarkTypeEnum.OVERSPREAD)
//                // 添加的水印还可以是图片
//                .image()
                .executor();
        /** 3、文件写入response输出流 **/
        // 设置下载的文件名称(filename属性就是设置下载的文件名称叫什么，通过字符类型转换解决中文名称为空的问题)
        String filename = URLEncoder.encode("水印_" + file.getName(), StandardCharsets.UTF_8.name());
        // 设置 Content-Disposition 响应头，告诉浏览器以下载方式处理该文件，并设置下载后的文件名。
        // 如果不设置此头，浏览器可能会尝试直接在窗口中打开文件（例如图片、PDF、文本等），或者下载的文件名可能不正确（如默认为接口名）。
        response.setHeader("content-disposition", "attachment;filename=" + filename);
        // 拼接文件存储路径
        try (
                InputStream innerInputStream = new ByteArrayInputStream(watermarkFile);
                OutputStream outputStream = response.getOutputStream();
        ) {
            // 缓冲区
            byte[] buffer = new byte[1024];
            // 读取文件流长度
            int len;
            // 读取到的文件内容没有结束，则写入输出流中
            while ((len = innerInputStream.read(buffer)) > 0) {
                // 将读取到的文件信息写入输出流，从0开始，读取到最后一位。
                // 不能省略off和len参数，因为如果文件结尾不够1024个字节那么outputStream.write(buffer)方法也会写入1024个字节，会导致文件信息丢失或被覆盖的问题
                outputStream.write(buffer, 0, len);
            }
            // 输出流的内容写入到文件（输出流的内容写入到磁盘）
            outputStream.flush();
        } catch (Exception e) {
            log.error("文件上传异常，异常信息：", e);
        }
    }
}

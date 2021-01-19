package com.wp.resource;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @Classname NewFileCopyTest
 * @Description 基于JDK7及以后实现正确关闭流资源的方式
 * try - with - resource
 * @Date 2021/1/19 15:50
 * @Created by wangpeng116
 */
public class NewFileCopyTest {
    public void copyFile() {
        // 定义输入路径和输出路径
        String originalUrl = "";
        String targetUrl = "";
        // 初始化输入输出流对象
        try (
                FileInputStream originalFileInputStream = new FileInputStream(originalUrl);
                FileOutputStream targetFileOutputStream = new FileOutputStream(targetUrl);
        ) {
            //读取的字节信息
            int content;
            while ((content = originalFileInputStream.read()) != -1) {
                targetFileOutputStream.write(content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

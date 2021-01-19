package com.wp.resource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Classname FileCopyTest
 * @Description JDK之前的文件拷贝功能
 * @Date 2021/1/19 15:34
 * @Created by wangpeng116
 */
public class FileCopyTest {
    public void copyFile() {
        /**
         * 1、创建输入/输出流
         * 2、执行文件拷贝，读取文件内容，写入到另一个文件中
         * 3、**关闭文件流资源**
         */
        // 定义输入路径和输出路径
        String originalUrl = "";
        String targetUrl = "";
        // 声明文件输入输出流
        FileInputStream originalFileInputStream = null;
        FileOutputStream targetFileOutputStream = null;
        try {
            // 实例化文件流对象
            originalFileInputStream = new FileInputStream(originalUrl);
            targetFileOutputStream = new FileOutputStream(targetUrl);

            //读取的字节信息
            int content;
            while ((content = originalFileInputStream.read()) != -1) {
                targetFileOutputStream.write(content);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流资源
            if (targetFileOutputStream != null) {
                try {
                    targetFileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (originalFileInputStream != null) {
                try {
                    originalFileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

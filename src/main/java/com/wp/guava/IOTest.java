package com.wp.guava;

import com.google.common.base.Charsets;
import com.google.common.io.CharSink;
import com.google.common.io.CharSource;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

/**
 * @Classname IOTest
 * @Description 通过Source(读文件)和Sink(写文件)来对I/O流操作
 * @Date 2021/1/26 13:53
 * @Created by wangpeng116
 */
public class IOTest {
    public static void main(String[] args) throws IOException {
        //1、创建对应的source和sink(这两个对象资源会自动关闭)
        CharSource charSource = Files.asCharSource(new File("SourceText.txt"), Charsets.UTF_8);
        CharSink charSink = Files.asCharSink(new File("TargetText.txt"), Charsets.UTF_8);
        /**
         * 拷贝：将SourceText.txt文件拷贝到TargetText.txt中
         */
        charSource.copyTo(charSink);
    }
}

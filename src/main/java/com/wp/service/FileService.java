package com.wp.service;

import java.io.File;
import java.io.InputStream;

/**
 * @Classname FileService
 * @Description 文件上传服务接口
 * @Date 2021/2/19 11:43
 * @Created by wangpeng116
 */
public interface FileService {
    /**
     * 文件上传
     *
     * @param inputStream
     * @param filename
     */
    void upload(InputStream inputStream, String filename);

    /**
     * 文化上传
     *
     * @param file
     */
    void upload(File file);
}

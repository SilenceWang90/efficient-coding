package com.wp.service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

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
     * 文件上传
     *
     * @param file
     */
    void upload(File file);

    /**
     * 文件下载
     *
     * @param fileId
     * @param response
     */
    void download(String fileId, HttpServletResponse response);

    /**
     * Excel导出
     *
     * @param response
     */
    void exportExcel(HttpServletResponse response);

    /**
     * 填充数据到固定模板(模板中通过{属性名}来确定要填充的内容)
     *
     * @param response
     */
    void exportWithFillDataInTemplate(HttpServletResponse response) throws UnsupportedEncodingException;

    /**
     * EasyExcel读取Excel
     *
     * @param inputStream
     */
    void readEasyExcel(InputStream inputStream);
}

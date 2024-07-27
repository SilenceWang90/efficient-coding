package com.wp.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wangpeng
 * @description Base64DealFileSerivce
 * @date 2024/7/26 11:56
 **/
public interface Base64DealFileSerivce {
    /**
     * 文件转base64
     */
    String fileToBase64Example(MultipartFile myfile);

    /**
     * base64转文件
     */
    void base64ToFileExample(String base64String, HttpServletResponse response) throws IOException;
}

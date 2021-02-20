package com.wp.controller;

import com.wp.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @Classname FileController
 * @Description 文件服务
 * @Date 2021/2/19 11:07
 * @Created by wangpeng116
 */
@RestController
@RequestMapping("/api/files")
@Slf4j
public class FileController {
    @Resource
    private FileService fileService;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public String upload(@NotNull MultipartFile file) {
        //文件上传...
        try {
            fileService.upload(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            log.error("文件上传失败，文件名称：{}，异常信息：{}", file.getOriginalFilename(), e);
        }
        return "上传成功";
    }

    /**
     * 文件下载
     *
     * @param fileId   一般下载需要文件id或者文件名称，具体看实际业务情况
     * @param response
     * @return
     */
    @GetMapping("/downloadFile")
    public String downloadFile(String fileId, HttpServletResponse response) {
        fileService.download(fileId, response);
        return "文件下载成功";
    }

    /**
     * 文件导出(excel)
     *
     * @param response
     * @return
     */
    @GetMapping("/exportExcel")
    public String exportExcel(HttpServletResponse response) {
        fileService.exportExcel(response);
        return "导出成功";
    }

    /**
     * 文件异步导出
     *
     * @param response
     * @return
     */
    @GetMapping("/asyncExportExcel")
    public String asyncExportExcel(HttpServletResponse response) {
        fileService.asyncExport(response);
        return "导出成功";
    }

    /**
     * 填充数据到固定模板(模板中通过{属性名}来确定要填充的内容)
     *
     * @param response
     */
    @GetMapping("/exportWithFillDataInTemplate")
    public String exportWithFillDataInTemplate(HttpServletResponse response) throws UnsupportedEncodingException {
        fileService.exportWithFillDataInTemplate(response);
        return "导出成功";
    }

    /**
     * EasyExcel读取文档
     *
     * @param file
     * @return
     */
    @PostMapping("/readEasyExcel")
    public String readEasyExcel(@NotNull MultipartFile file) throws IOException {
        fileService.readEasyExcel(file.getInputStream());
        return "读取成功";
    }
}


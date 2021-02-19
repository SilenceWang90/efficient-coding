package com.wp.service.impl;

import com.wp.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @Classname FileServiceImpl
 * @Description 文件上传服务实现
 * @Date 2021/2/19 11:55
 * @Created by wangpeng116
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    /**
     * 存储空间
     */
    private static final String BUCKET = "C:\\Users\\wangpeng116\\Desktop";

    @Override
    public void upload(InputStream inputStream, String filename) {
        // 拼接文件存储路径
        String storagePath = BUCKET + "/" + filename;
        try (
                // JDK8 TWR不能关闭外部资源，因此定义内部流来实现对外部流的关闭
                InputStream innerInputStream = inputStream;
                FileOutputStream outputStream = new FileOutputStream(new File(storagePath))
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
            log.error("文件上传异常，文件名称：{}，异常信息：{}", filename, e);
        }
    }

    @Override
    public void upload(File file) {
        try {
            upload(new FileInputStream(file), file.getName());
        } catch (Exception e) {
            log.error("文件上传异常，文件名称：{}，异常信息：{}", file.getName(), e);
        }
    }

    @Override
    public void download(String fileId, HttpServletResponse response) {
        //文件路径，一般是通过文件id获取，实际要根据实际的业务规则
        String filePath = "F:/审批通过数据.xlsx";
        File file = new File(filePath);
        try (
                OutputStream outputStream = response.getOutputStream();
                InputStream inputStream = new FileInputStream(file)
        ) {
            //设置下载的文件名称(filename属性就是设置下载的文件名称叫什么，通过字符类型转换解决中文名称为空的问题)
            String filename = new String(file.getName().getBytes("GBK"), StandardCharsets.ISO_8859_1);
            response.setHeader("content-disposition", "attachment;filename=" + filename);
//            response.setContentType("application/octet-stream;charset=UTF-8");
            // 缓冲区
            byte[] buffer = new byte[1024];
            // 读取文件流长度
            int len;
            // 读取到的文件内容没有结束，则写入输出流中
            while ((len = inputStream.read(buffer)) > 0) {
                // 将读取到的文件信息写入输出流，从0开始，读取到最后一位。
                // 不能省略off和len参数，因为如果文件结尾不够1024个字节那么outputStream.write(buffer)方法也会写入1024个字节，会导致文件信息丢失或被覆盖的问题
                outputStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            log.error("文件下载失败，文件路径：{}，异常信息：{}", filePath, e);
        }

    }
}

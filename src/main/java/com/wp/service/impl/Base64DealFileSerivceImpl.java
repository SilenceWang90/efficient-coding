package com.wp.service.impl;

import com.wp.service.Base64DealFileSerivce;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Base64;

/**
 * @author wangpeng
 * @description Base64DealFileSerivceImpl
 * @date 2024/7/26 11:57
 **/
@Service
public class Base64DealFileSerivceImpl implements Base64DealFileSerivce {
    @Override
    public String fileToBase64Example(MultipartFile file) {
        String base64String = "";
        try (InputStream fis = file.getInputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            byte[] fileBytes = baos.toByteArray();
            base64String = Base64.getEncoder().encodeToString(fileBytes);
            System.out.println(base64String);
            // Optionally, save base64String to a text file
            // Files.write(Paths.get("example_base64.txt"), base64String.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64String;
    }

    @Override
    public void base64ToFileExample(String base64String, HttpServletResponse response) throws IOException {
//        response.setHeader("content-disposition", "attachment;filename=convert.docx");
//        response.setContentType("application/octet-stream;charset=UTF-8");
        /** 小文件可以直接通过输出流的write()函数将文件信息写入输出流中 **/
        // 传文档的时候一定要告知文件类型才可以！！！
        File file = new File("/Users/manman/Desktop/conversion.docx");
        // 解码Base64字符串
        byte[] decodedBytes = Base64.getDecoder().decode(base64String);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(decodedBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /** 大文件不能像上面一样把文件一次写入，要一次写一部分，防止文件过大造成问题 **/
        // 创建文件输出流
        /*try (FileOutputStream fos = new FileOutputStream("/Users/manman/Desktop/conversion.xlsx");
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(base64String))) {
            // 创建一个缓冲区
            byte[] buffer = new byte[8192]; // 可以根据需要调整缓冲区大小
            int bytesRead;
            // 通过缓冲区逐块读取数据
            while ((bytesRead = bais.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            // 处理异常，例如记录日志
        }*/
    }
}

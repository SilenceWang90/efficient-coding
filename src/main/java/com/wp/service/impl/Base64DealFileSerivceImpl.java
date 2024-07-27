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
        File file = new File("/Users/manman/Desktop/conversion.docx");
        // 解码Base64字符串
        byte[] decodedBytes = Base64.getDecoder().decode(base64String);
        try (FileOutputStream fos = new FileOutputStream(file)) {
//            response.getOutputStream().write(decodedBytes);
            fos.write(decodedBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

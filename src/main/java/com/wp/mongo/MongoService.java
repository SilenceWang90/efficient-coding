package com.wp.mongo;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSUploadStream;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author wangpeng
 * @description MongoService
 * @date 2024/7/25 09:24
 **/
@Service
public class MongoService {
    @Autowired
    private GridFSBucket gridFSBucket;

    public String uploadFile(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            GridFSUploadStream uploadStream = gridFSBucket.openUploadStream(file.getOriginalFilename());
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                uploadStream.write(buffer, 0, length);
            }
            uploadStream.close();
            ObjectId fileId = uploadStream.getObjectId();
            return fileId.toHexString();
        }
    }

    public ResponseEntity<Resource> downloadFile(String fileId) {
        GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(new ObjectId(fileId));
        // 将GridFSDownloadStream中的数据读入ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = downloadStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        // 从ByteArrayOutputStream创建InputStream
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        String filename = new String(downloadStream.getGridFSFile().getFilename().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

        return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
    }
}

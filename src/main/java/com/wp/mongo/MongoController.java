package com.wp.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author wangpeng
 * @description MongoController
 * @date 2024/7/25 09:18
 **/
@RestController
@RequestMapping("/mongo")
public class MongoController {
    @Autowired
    private MongoService mongoService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileId = mongoService.uploadFile(file);
            return ResponseEntity.ok(fileId);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        }
    }

    @GetMapping("download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String id) {
        return mongoService.downloadFile(id);
    }
}

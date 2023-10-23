package com.wp.feign.feignapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @Author wangpeng
 * @Date 2023/10/23 11:02
 */
@FeignClient(name = "file-service", path = "/api/files", url = "http://localhost:8080")
public interface FileServiceApi {
    @GetMapping("/batchDownloadPath")
    void batchDownloadPath(@RequestParam("sourceFolderPath") String sourceFolderPath
            , HttpServletResponse response);
}

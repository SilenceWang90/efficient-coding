package com.wp.controller;

import com.wp.service.Base64DealFileSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wangpeng
 * @description TestBase64Controller
 * @date 2024/7/26 13:19
 **/
@RestController
@RequestMapping("/testBase64")
public class TestBase64Controller {
    @Autowired
    private Base64DealFileSerivce base64DealFileSerivce;

    @RequestMapping("/fileToBase64Str")
    public String fileToBase64Str(@RequestParam("myfile") MultipartFile myfile) {
        return base64DealFileSerivce.fileToBase64Example(myfile);
    }

    @RequestMapping("/base64StrToFile")
    public void base64StrToFile(@RequestBody String base64String, HttpServletResponse response) throws IOException {
        base64DealFileSerivce.base64ToFileExample(base64String,response);
    }
}

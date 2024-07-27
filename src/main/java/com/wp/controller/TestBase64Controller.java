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

    /**
     * 将文件转成base64字符串
     *
     * @param myfile
     * @return
     */
    @RequestMapping("/fileToBase64Str")
    public String fileToBase64Str(@RequestParam("myfile") MultipartFile myfile) {
        return base64DealFileSerivce.fileToBase64Example(myfile);
    }

    /**
     * 将base64字符串还原成文件
     *
     * @param base64String 文件转换成的base64字符串
     * @param response
     * @throws IOException
     */
    @RequestMapping("/base64StrToFile")
    public void base64StrToFile(@RequestBody String base64String, HttpServletResponse response) throws IOException {
        System.out.println("获取的字符串信息为：" + base64String);
        // postman传json的纯字符串的时候一般需要加上""，因为对于一些长文本尤其是经过base64处理过后的字符串可能会出现一些特殊的字符串比如"31改35y//fwfe",postman遇到//时也会按照注释处理
        // 这样//之后的内容就无法传给后台了导致字符串信息丢失。因此需要在整个字符串上加上""防止postman特殊解析处理。
        // 但是加上了""之后springmvc的消息转换器会保留这个""，因此这里要把收到的字符串首尾多余的""给去掉
        /** 最好用下面的方法接收参数，用标准的json数据结构传比较好，而且因为需要知道文件的类型和名称，因此传文件肯定不能只传文件本身。 **/
        base64String = base64String.substring(1, base64String.length() - 1);
        base64DealFileSerivce.base64ToFileExample(base64String, response);
    }

    /**
     * 用常规json结构传
     * @param base64StringMap
     * @param response
     * @throws IOException
     */
    /*@RequestMapping("/base64StrToFile")
    public void base64StrToFile(@RequestBody Map<String, String> base64StringMap, HttpServletResponse response) throws IOException {
        System.out.println("获取的字符串信息为：" + base64StringMap.get("fileStr"));
        base64DealFileSerivce.base64ToFileExample(base64StringMap.get("fileStr"), response);
    }*/
}

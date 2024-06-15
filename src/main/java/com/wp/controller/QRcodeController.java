package com.wp.controller;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.wp.util.QRCodeGeneratorUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * @Description 生成二维码
 * @Author admin
 * @Date 2024/6/15 14:29
 */
@Controller
@RequestMapping("/qrcode")
public class QRcodeController {
    @GetMapping("/generateQRcode")
    public ModelAndView generateQRcode(@RequestParam("text") String text
            , @RequestParam("width") int width
            , @RequestParam("height") int height) throws IOException, WriterException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("qrcode.html");
        String qrcodeImage = QRCodeGeneratorUtil.generateQRCodeImage(text, width, height);
        modelAndView.addObject("QRCode", qrcodeImage);
        return modelAndView;
    }

    /**
     * @param multipartFile
     * @return
     */
    @GetMapping("/readQRCode")
    @ResponseBody
    public String readQRCode(@RequestParam("file") MultipartFile multipartFile) throws IOException, NotFoundException {
        return QRCodeGeneratorUtil.readQRCodeImg(multipartFile.getInputStream());
    }
}

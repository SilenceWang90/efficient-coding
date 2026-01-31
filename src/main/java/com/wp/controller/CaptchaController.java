package com.wp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * @author wangpeng
 * @description 验证码
 * @date 2026/1/31 11:00
 **/
@Controller
@RequestMapping("/captcha")
public class CaptchaController {
    /**
     * 显示验证页面
     * @return 页面名称
     */
    @GetMapping("/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("verificationcode.html");
        return modelAndView;
    }

//    /**
//     * 获取验证码图片（基于Session）
//     * @return Base64编码的验证码图片
//     */
//    @GetMapping("/captcha")
//    @ResponseBody
//    public ResponseEntity<String> getCaptcha() {
//        try {
//            String imageBase64 = captchaService.generateCaptcha();
//            return ResponseEntity.ok(imageBase64);
//        } catch (IOException e) {
//            return ResponseEntity.status(500).body("生成验证码失败");
//        }
//    }
//
//    /**
//     * 验证答案（基于Session）
//     * @param answer 用户输入的答案
//     * @return 验证结果
//     */
//    @PostMapping("/validate")
//    @ResponseBody
//    public ResponseEntity<String> validateCaptcha(@RequestParam String answer) {
//        System.out.println("收到验证请求，答案: " + answer);
//        boolean isValid = captchaService.validateCaptcha(answer);
//        if (isValid) {
//            return ResponseEntity.ok("验证成功");
//        } else {
//            return ResponseEntity.badRequest().body("验证失败");
//        }
//    }
}

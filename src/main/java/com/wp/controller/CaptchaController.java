package com.wp.controller;

import com.wp.util.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

/**
 * @author wangpeng
 * @description 验证码
 * @date 2026/1/31 11:00
 **/
@Controller
@RequestMapping("/captcha")
public class CaptchaController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 显示验证页面
     *
     * @return 页面名称
     */
    @GetMapping("/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("verificationcode.html");
        return modelAndView;
    }

    /**
     * 获取验证码图片
     *
     * @return Base64编码的验证码图片
     */
    @GetMapping("/getCaptcha")
    @ResponseBody
    public CaptchaUtil.CaptchaResponse getCaptcha() {
        /** 1、创建验证码表达式以及表达式结果 **/
        CaptchaUtil.MathExpression mathExpression = CaptchaUtil.generateMathExpression();
        /** 2、创建验证码图片信息 **/
        CaptchaUtil.CaptchaResponse captchaResponse = new CaptchaUtil.CaptchaResponse();
        try {
            // 2.1、创建验证码图片base64字符串
            String imageBase64 = CaptchaUtil.generateCaptchaImage(mathExpression.getExpression());
            // 2.2、生成验证码的唯一ID
            String captchaId = UUID.randomUUID().toString();
            // 分布式集群环境中要将验证码的信息存储在redis中，便于后续的验证
            stringRedisTemplate.opsForValue().set(captchaId, String.valueOf(mathExpression.getResult()), Duration.ofSeconds(20));
            captchaResponse.setCaptchaId(captchaId);
            captchaResponse.setImageData(imageBase64);
            return captchaResponse;
        } catch (IOException e) {
            return captchaResponse;
        }
    }

//    /**
//     * 验证答案
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

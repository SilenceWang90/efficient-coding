package com.wp.controller;

import com.wp.util.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
            /** 3、封装图片信息返回值 **/
            captchaResponse.setCaptchaId(captchaId);
            captchaResponse.setImageData(imageBase64);
            return captchaResponse;
        } catch (IOException e) {
            return captchaResponse;
        }
    }

    /**
     * 验证答案
     *
     * @param captchaId  验证码图片id
     * @param userAnswer 用户输入的答案
     * @return 验证结果
     */
    @PostMapping("/validateCaptcha")
    @ResponseBody
    public Boolean validateCaptcha(@RequestParam("captchaId") String captchaId, @RequestParam("answer") String userAnswer) {
        try {
            /** 1、获取验证码正确答案 **/
            int correctAnswer = Integer.parseInt(stringRedisTemplate.opsForValue().get("captchaId"));
            int answer = Integer.parseInt(userAnswer);
            System.out.println("验证码ID: " + captchaId);
            System.out.println("用户输入答案: " + answer);
            System.out.println("正确答案: " + correctAnswer);
            /** 2、验证答案是否正确 **/
            if (answer == correctAnswer) {
                System.out.println("验证成功");
                return true;
            } else {
                System.out.println("验证失败");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("数字格式错误: " + userAnswer);
            return false;
        }
    }
}

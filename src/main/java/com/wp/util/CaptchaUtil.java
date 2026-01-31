package com.wp.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

/**
 * @author wangpeng
 * @description 验证码工具类
 * @date 2026/1/31 11:16
 **/
public class CaptchaUtil {

    /**
     * 私有化工具类，防止额外创建多余的工具类
     */
    private CaptchaUtil() {
    }

    /**
     * 生成随机数学表达式和答案
     *
     * @return MathExpression 包含表达式和答案的对象
     */
    public static MathExpression generateMathExpression() {
        Random random = new Random();
        int a = random.nextInt(10) + 1; // 1-10
        int b = random.nextInt(10) + 1; // 1-10
        // 随机决定是加法还是减法
        String operator;
        int result;
        if (random.nextBoolean()) {
            operator = "+";
            result = a + b;
        } else {
            operator = "-";
            // 确保结果不为负数
            if (a < b) {
                int temp = a;
                a = b;
                b = temp;
            }
            result = a - b;
        }
        String expression = a + " " + operator + " " + b + " = ?";
        return new MathExpression(expression, result);
    }

    /**
     * 生成验证码图片
     *
     * @param expression 数学表达式
     * @return Base64编码的图片字符串
     * @throws IOException IO异常
     */
    public static String generateCaptchaImage(String expression) throws IOException {
        int width = 120;
        int height = 40;
        /** 1、创建图片对象（相当于创建画布） **/
        // BufferedImage.TYPE_INT_RGB参数：支持透明背景（比如生成的 PNG LOGO 图标）
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        /** 2、Graphics2D，通过Graphics2D在画布BufferedImage上进行绘图 **/
        Graphics2D graphics = image.createGraphics();
        // 设置画笔为白色
        graphics.setColor(Color.WHITE);
        // 用当前的画笔画满BufferedImage画布，相当于设置背景色。BufferedImage新建的时候如果不指明颜色则默认全黑
        // (x,y)为绘画起点，width、height为指定矩形的宽度和高度。即从起点(x,y)出发，绘制一个宽为width，高为height的一个矩形。
        graphics.fillRect(0, 0, width, height);
        // 设置字体
        graphics.setFont(new Font("Arial", Font.BOLD, 16));
        // 绘制5条干扰线（让验证码更难被机器识别）
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            // 设置当前干扰线的颜色
            graphics.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            // 绘制干扰线。在(x1,y1)和(x2,y2)两个点之间绘制一条直线
            graphics.drawLine(x1, y1, x2, y2);
        }
        /** 3、绘制表达式文字 **/
        // 设置画笔为黑色
        graphics.setColor(Color.BLACK);
        // 传入验证码文本内容。x、y即文本坐标位置
        graphics.drawString(expression, 10, 25);
        // 画完，释放资源
        graphics.dispose();
        // 将图片转换为Base64字符串
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] bytes = baos.toByteArray();
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 内部类用于存储生成的表达式和结果
     */
    public static class MathExpression {
        private String expression;
        private int result;

        public MathExpression(String expression, int result) {
            this.expression = expression;
            this.result = result;
        }

        public String getExpression() {
            return expression;
        }

        public int getResult() {
            return result;
        }
    }

}

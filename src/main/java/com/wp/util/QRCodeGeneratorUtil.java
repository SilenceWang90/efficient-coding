package com.wp.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author admin
 * @Date 2024/6/15 14:43
 */
@Slf4j
public class QRCodeGeneratorUtil {
    private QRCodeGeneratorUtil() {
    }

    ;

    /**
     * 生成二维码
     *
     * @param text   二维码对应的文本内容
     * @param width  二维码宽度
     * @param height 二维码长度
     * @return
     */
    public static String generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        /**1、创建QRCodeWriter，用于生成QRCode二维码**/
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        // 1.1、二维码的属性设置：如编码格式、纠错等级、二维码的间距等等信息
        Map<EncodeHintType, Object> hints = new HashMap<>();
        // 设置文本编码格式为UTF-8用以支持中文
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);
        // 设置纠错等级，纠错等级越高，二维码损坏后依然能识别的能力就越强，相对的，能存储的信息就越少(即text的内容就越少)
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        // 1.2、创建二维码，将二维码的内容text、长度宽度、设置的属性hints作为配置传入。其中BarcodeFormat为二维码的标准，我们生成常用的QRCode标准的二维码即可
        // BitMatrix就是二维码的黑白矩阵信息
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        /** 2、生成二维码图片信息 **/
        // 2.1、将二维码信息创建为二维码图像对象(注意bufferedImage并非最终的图片本身即我们已知的图片文件，bufferedImage只是图片文件中的所有具体内容都在此对象中)
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        // 2.2、将图片信息对象具象化为图片文件。
        // 为了方便在内存中操作图片，将图片写入输出流，输出流便于转成byte[]数组，这样生成的图片就方便在java内存中进行各种操作，而不是直接生成张图片放在服务器上，不方便代码操作~~~~~~
        // 如果一个接口需要直接返回一张图片，就从response对象中获取outputstream，通过ImageIO将bufferedImage其写入response.outputstream即可；否则如下就直接创建一个即可。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 通过ImageIO创建图像文件，并写入指定的输出流中
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        // 2.2、将图片信息转成base64字符串，方便在java代码中操作(字符串的信息就是一张完整的图片信息，而不是在服务器磁盘上直接生成一张图片)，并和前端进行数据交互
        byte[] pngData = byteArrayOutputStream.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(pngData);
        return base64Image;
    }
}

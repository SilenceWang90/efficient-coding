package com.wp.util;

import com.google.common.collect.Maps;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
        // 还可以写入outputstream中或者某个path中，一般都是像demo这么写。其他两种方式暂未尝试
        File file = new File("C:\\Users\\admin\\Desktop\\qrcode.png");
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", file.toPath());
        return "base64Image";
    }

    /**
     * 解析二维码文件
     *
     * @param qrcodeInputStream 二维码文件输入流
     * @return 二维码的内容
     * @throws IOException
     * @throws NotFoundException
     */
    public static String readQRCodeImg(InputStream qrcodeInputStream) throws IOException, NotFoundException {
        // 通过BufferedImage读取输入流文件，便于后续的解码
        BufferedImage bufferedImage = ImageIO.read(qrcodeInputStream);
        // 将BufferedImage转成BinaryBitmap，解码的方法需要解析BufferedImage
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
        // 解码参数，如中文编码设置
        Map<DecodeHintType, Object> hints = Maps.newHashMap();
        hints.put(DecodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);
        // 解码，解码后的对象为Result，text属性中存储着二维码存储的信息
        Result result = new MultiFormatReader().decode(binaryBitmap, hints);
        return result.getText();
    }
}

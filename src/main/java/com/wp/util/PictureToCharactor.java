package com.wp.util;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

import java.io.File;

/**
 * @Classname PictureToCharactor
 * @Description 图片文字识别：建议使用百度的服务
 * @Date 2021/8/25 13:32
 * @Created by wangpeng116
 */
public class PictureToCharactor {
    public static void main(String[] args) {
        File picture = new File("");
        ITesseract instance = new Tesseract();
        /* 使用需要依赖本地tesseractocr服务，需要安装到服务器上，
        具体可以参考：https://blog.csdn.net/wsk1103/article/details/54173282 */
    }
}

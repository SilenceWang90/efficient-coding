package com.wp.generate_file;

//import com.deepoove.poi.XWPFTemplate;
//import com.deepoove.poi.data.Pictures;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangpeng
 * @description 文档生成
 * @date 2024/8/25 10:05
 **/
@RestController
@RequestMapping("/generateFile")
@Slf4j
public class GenerateFileController {
    /**
     * 填充并导出excel
     */
    @RequestMapping("generateExcel")
    public void generateExcel() {
        log.info("参见笔记《一、生成Excel》");
    }

    /**
     * 填充并导出word，依赖冲突，可以参见笔记找一个干净的项目验证即可。
     */
//    @RequestMapping("generateWord")
//    public void generateWord(HttpServletResponse response) throws IOException {
//        log.info("参见笔记《二、生成Word》");
//        /** 1、准备填入的数据 **/
//        // 1.1、文本数据
//        Map<String, Object> data = new HashMap<>();
//        data.put("name", "张三");
//        data.put("age", 25);
//        data.put("address", "北京");
//        // 1.2、图片数据
//        data.put("pic", Pictures.ofStream(new FileInputStream("/Users/manman/Desktop/my_picture.jpg")).size(150, 150).create());
//        /** 2、读取要填充的word模板文档 **/
//        InputStream templateInputStream = getClass().getClassLoader()
//                .getResourceAsStream("templates/word_template.docx");
//        /** 3、渲染模板 **/
//        XWPFTemplate template = XWPFTemplate.compile(templateInputStream).render(data);
//        /** 4、word生成，返回给response输出流 **/
//        String filename = new String("测试word填充结果.docx".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
//        response.addHeader("Content-Disposition", "attachment;fileName=" + filename);
//        OutputStream outputStream = response.getOutputStream();
//        template.write(outputStream);
//        template.close();
//    }

    /**
     * 填充并导出pdf
     */
    @RequestMapping("generatePdf")
    public void generatePdf() {
        log.info("参见笔记《三、生成PDF》");
    }

    /**
     * 填充并导出ppt
     */
    @RequestMapping("generatePpt")
    public void generatePpt() {
        log.info("参见笔记《四、生成PPT");
    }
}

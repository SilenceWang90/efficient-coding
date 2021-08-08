package com.wp.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wp.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Classname FileController
 * @Description 文件服务
 * @Date 2021/2/19 11:07
 * @Created by wangpeng116
 */
@RestController
@RequestMapping("/api/files")
@Slf4j
public class FileController {
    @javax.annotation.Resource
    private FileService fileService;

    @javax.annotation.Resource
    private RestTemplate restTemplate;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public String upload(@NotNull MultipartFile file) {
        //文件上传...
        try {
            fileService.upload(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            log.error("文件上传失败，文件名称：{}，异常信息：{}", file.getOriginalFilename(), e);
        }
        return "上传成功";
    }

    /**
     * 文件下载
     *
     * @param fileId   一般下载需要文件id或者文件名称，具体看实际业务情况
     * @param response
     * @return
     */
    @GetMapping("/downloadFile")
    public String downloadFile(String fileId, HttpServletResponse response) {
        fileService.download(fileId, response);
        // 对于文件下载功能，为了保证文件可以被最终下载有时需要定义响应信息，移动端安卓系统可能就需要此配置来保证文件可以正常下载
        /*response.setHeader("content-disposition", "attachment;filename=" + fileFullNameWithSuffix);
        response.setContentType("application/octet-stream;charset=UTF-8");*/
        return "文件下载成功";
    }

    /**
     * 文件异步导出
     *
     * @param response
     * @return
     */
    @GetMapping("/asyncExportExcel")
    public String asyncExportExcel(HttpServletResponse response) {
        fileService.asyncExport(response);
        return "导出成功";
    }

    /**
     * 填充数据到固定模板(模板中通过{属性名}来确定要填充的内容)
     *
     * @param response
     */
    @GetMapping("/exportWithFillDataInTemplate")
    public String exportWithFillDataInTemplate(HttpServletResponse response) throws UnsupportedEncodingException {
        fileService.exportWithFillDataInTemplate(response);
        return "导出成功";
    }

    /**
     * EasyExcel读取文档
     *
     * @param file
     * @return
     */
    @PostMapping("/readEasyExcel")
    public String readEasyExcel(@NotNull MultipartFile file) throws IOException {
        fileService.readEasyExcel(file.getInputStream());
        return "读取成功";
    }

    @GetMapping("/exportExcelByte")
    public byte[] exportExcel() throws IOException {
        //设置下载的文件名称(filename属性就是设置下载的文件名称叫什么，通过字符类型转换解决中文名称为空的问题)
        String filename = new String("byte数组的Excel文件".getBytes("GBK"), StandardCharsets.ISO_8859_1);
        String filePath = "F:/审批通过数据.xlsx";
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = inputStream.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }


    /**
     * 从其他接口获取文件信息
     */
    @GetMapping("/getFileInfoByte")
    public void getFileInfoByte(HttpServletResponse response) throws IOException {
        ResponseEntity<byte[]> entity = restTemplate.getForEntity("http://localhost:8080/api/files/exportExcelByte", byte[].class);
        // 别人的输出就是我的输入
        byte[] bytes = entity.getBody();
        //设置下载的文件名称(filename属性就是设置下载的文件名称叫什么，通过字符类型转换解决中文名称为空的问题)
        String filename = new String("我是导出的excelByte.xlsx".getBytes("GBK"), StandardCharsets.ISO_8859_1);
        response.setHeader("content-disposition", "attachment;filename=" + filename);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        System.out.println("下载成功");
    }


    /**
     * 文件导出(excel)
     *
     * @param response
     * @return
     */
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response) {
        fileService.exportExcel(response);
    }


    /**
     * 从其他接口获取文件信息
     */
    @GetMapping("/getFileInfo")
    public void getFileInfo(HttpServletResponse response) throws IOException {
        ResponseEntity<Resource> entity = restTemplate.getForEntity("http://localhost:8080/api/files/exportExcel", Resource.class);
        // 别人的输出就是我的输入
        InputStream inputStream = entity.getBody().getInputStream();
        OutputStream outputStream = response.getOutputStream();
        //设置下载的文件名称(filename属性就是设置下载的文件名称叫什么，通过字符类型转换解决中文名称为空的问题)
        String filename = new String("我是导出的excel.xlsx".getBytes("GBK"), StandardCharsets.ISO_8859_1);
        response.setHeader("content-disposition", "attachment;filename=" + filename);
        // 缓冲区
        byte[] buffer = new byte[1024];
        // 读取文件流长度
        int len;
        // 读取到的文件内容没有结束，则写入输出流中
        while ((len = inputStream.read(buffer)) > 0) {
            // 将读取到的文件信息写入输出流，从0开始，读取到最后一位。
            // 不能省略off和len参数，因为如果文件结尾不够1024个字节那么outputStream.write(buffer)方法也会写入1024个字节，会导致文件信息丢失或被覆盖的问题
            outputStream.write(buffer, 0, len);
        }
        System.out.println("下载");
    }

    /**
     * 获取doc文件，并填充doc文件信息
     *
     * @param response
     * @throws IOException
     */
    @GetMapping("/getDocFileInfo")
    public void getDocFileInfo(HttpServletResponse response) throws Exception {
        String filePath = "F:/测试word填充模板.docx";
        String filename = new String("测试word填充结果.docx".getBytes("GBK"), StandardCharsets.ISO_8859_1);
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        XWPFDocument document = new XWPFDocument(inputStream);
        Map<String, Object> mapParams = Maps.newHashMap();
        mapParams.put("name", "王鹏");
        mapParams.put("age", "30");
        response.addHeader("Content-Disposition", "attachment;fileName=" + filename);
        OutputStream outputStream = response.getOutputStream();
        document.write(outputStream);
        outputStream.close();
    }


    /**
     * 获取doc文件，并填充doc文件信息：注意，如果分词中获得的run不正确，在文本文档中打好文字再复制粘贴到word中
     *
     * @param response
     * @throws IOException
     */
    @GetMapping("/getDocFileInfoByPoi")
    public void getDocFileInfoByPoi(HttpServletResponse response) throws Exception {
        /** 1、获取文件 */
        String filePath = "F:/测试word填充模板.docx";
        String filename = new String("测试word填充结果.docx".getBytes("GBK"), StandardCharsets.ISO_8859_1);
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        XWPFDocument document = new XWPFDocument(inputStream);
        /** 2、封装参数 */
        Map<String, String> mapParams = Maps.newHashMap();
        // 模板中的括号自行删除
        mapParams.put("{{", "");
        mapParams.put("}}", "");
        // 模板中的其他数据进行替换
        mapParams.put("name", "王鹏");
        mapParams.put("age", "30");
        mapParams.put("address", "大连沈阳北京");
        /** 3、获取word文档中的内容，替换模板信息 */
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        // 循环段落信息(按行来的，只要该行存在{{就会进行替换处理)
        for (XWPFParagraph paragraph : paragraphs) {
            String text = paragraph.getText();
            // 检索该行文档中的所有文本，判断此段落是否需要替换
            if (checkText(text)) {
                List<XWPFRun> runs = paragraph.getRuns();
                // 相当于分词，针对需要替换的词进行处理
                for (XWPFRun run : runs) {
                    // 替换模板中占位符的值
                    String ob = changeValue(run.toString(), mapParams);
                    if (mapParams.containsKey(run.toString())) {
                        run.setText(ob, 0);
                    }
                }
            }
        }
        /** 4、输出word文档 */
        response.addHeader("Content-Disposition", "attachment;fileName=" + filename);
        OutputStream outputStream = response.getOutputStream();
        document.write(outputStream);
        outputStream.close();
    }

    /**
     * 检查文本中是否包含指定的字符(此处为“{{}}”)，并返回值
     *
     * @param text 文档内容
     * @return 文档是否包含特殊占位符
     */
    public static boolean checkText(String text) {
        boolean check = false;
        if (text.contains("{{")) {
            check = true;
        }
        return check;
    }

    /**
     * @param value      分词之后的词组
     * @param dataParams 参数替换mao
     * @return
     */
    public static String changeValue(String value, Map<String, String> dataParams) {
        Set<Map.Entry<String, String>> textSets = dataParams.entrySet();
        String resultValue = "";
        for (Map.Entry<String, String> textSet : textSets) {
            // 匹配模板与替换值 格式{{key}}
            String key = textSet.getKey();
            if (value.contains(key)) {
                resultValue = textSet.getValue();
            }
        }
        return resultValue;
    }

    public static List<String> regMatcher(String text) {
        // 1、\\{需要对大括号转义，否则正则中大括号中应该是数字，idea会报错
        // 2、[a-zA-Z0-9]+表示兼容多个字母，如果没有+号则[a-zA-Z0-9]表示只有一个字母或数字才能匹配上
        String regStr = "\\{\\{[a-zA-Z0-9]+}}";
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(text);
        List<String> matcherStrs = Lists.newArrayList();
        while (matcher.find()) {
            matcherStrs.add(matcher.group());
        }
        return matcherStrs;
    }


    /*public static void main(String[] args) {
        String text = "大家好，my name is S1mple，我是{{name}}，我住在{{age}}。";
        // 1、\\{需要对大括号转义，否则正则中大括号中应该是数字，idea会报错
        // 2、[a-zA-Z0-9]+表示兼容多个字母，如果没有+号则[a-zA-Z0-9]表示只有一个字母或数字才能匹配上
        String regStr = "\\{\\{[a-zA-Z0-9]+}}";
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(text);
        List<String> matcherStrs = Lists.newArrayList();
        while (matcher.find()) {
            matcherStrs.add(matcher.group());
        }
        System.out.println(matcherStrs.toString());
    }*/
}


package com.wp.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wp.service.FileService;
import com.wp.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

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
     * @param myfile 不写请求参数注解也可以，类似使用@RequestParam注解的方式，MultipartFile对象的名称要和请求参数中的file组件的参数对应即可
     * @return
     */
    @PostMapping("/upload")
    public String upload(@RequestParam("myfile") MultipartFile myfile,@RequestParam("wpName") String wpName) {
        //文件上传...
        try {
            fileService.upload(myfile.getInputStream(), myfile.getOriginalFilename());
        } catch (IOException e) {
            log.error("文件上传失败，文件名称：{}，异常信息：{}", myfile.getOriginalFilename(), e);
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
        String filename = new String("byte数组的Excel文件".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
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
        String filename = new String("我是导出的excelByte.xlsx".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
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
        String filename = new String("我是导出的excel.xlsx".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
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
        String filename = new String("测试word填充结果.docx".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
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
        String filename = new String("测试word填充结果.docx".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
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


    /**
     * zip文件上传并解压
     *
     * @param sourceFile zip包文件
     */
    @RequestMapping("/batchUpload")
    public void batchUpload(@RequestParam("batchFile") @NotNull MultipartFile sourceFile) {
        File file = null;
        ZipFile zipFile = null;
        try (
                InputStream inputStream = sourceFile.getInputStream()
        ) {
            // zip文件放在临时目录，该参数可以根据需要配置
            file = new File("F:\\tempwp.zip");
            // 需要apache的commons-io包
            // 将上传的文件放到file中
            /** 1、将multipartfile转成zipfile*/
            FileUtils.copyInputStreamToFile(inputStream, file);
            // 解决文件名称中，中文字符集编码错误的问题
            zipFile = new ZipFile(file, Charset.forName("GBK"));
            /** 2、解压zip包获取所有文件*/
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            // 解压的文件放在目标目录，该参数可以根据需要配置
            String target = "F:\\";
            /** 3、遍历zip包的所有文件*/
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                // 当前解压的文件
                File currentUnzipFile = new File(target, zipEntry.getName());
                // 获取当前文件的文件流
                InputStream in = zipFile.getInputStream(zipEntry);
                OutputStream out = new FileOutputStream(currentUnzipFile);
                // 文件信息写入到输出流
                IOUtils.copy(in, out);
                // 每写完一个文件即关闭输出流
                out.close();
            }
        } catch (Exception e) {
            log.error("压缩文件上传并解压失败：{},文件名称：{}", e, sourceFile.getName());
        } finally {
            /** 4、删除zip文件(一般解压后不需要原zip文件，因此为了节省空间删除zip文件)。切记一定要关闭文件流才可以删除文件，否则资源被占用无法删除*/
            try {
                zipFile.close();
            } catch (IOException e) {
                log.error("zip压缩包文件删除失败：{}，文件名称：{}", e, sourceFile.getName());
            }
            file.delete();
        }
    }

    /**
     * 打包下载(zip包)，对多个指定文件
     *
     * @param response zip文件返回
     */
    @RequestMapping("/batchDownload")
    public void batchDownload(HttpServletResponse response) throws IOException {
        String zipFileName = "压缩包文件.zip";
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(zipFileName, "UTF-8"));
        response.setContentType("application/octet-stream;charset=UTF-8");
        /** 1、获取源文件(要打包下载的文件)*/
        File file1 = new File("/Users/mlamp/Desktop/工作/思路以及问题汇总.txt");
        File file2 = new File("/Users/mlamp/Desktop/工作/工程initializer脚手架.xlsx");
        List<File> files = Lists.newArrayList(file1, file2);
        try (
                /** 2、得到ZipOutputStream用于生成zip文件*/
                // 将文件输出到指定位置还是直接输出到response的输出流根据业务需要决定选择即可～
                /*// 最终的压缩文件输出到指定目录
                OutputStream zipFileOutputStream = new FileOutputStream("/Users/mlamp/wangpeng/temp/压缩包文件.zip");*/
                // 最终的压缩文件输出到resonse输出流
                OutputStream outputStream = response.getOutputStream();
                // 获得zip输出流
                ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)
        ) {
            /** 3、遍历file文件信息，将源文件写入zip输出流中*/
            int i = 0;
            for (File file : files) {
                /** 知识点：
                 * 1、如果打包文件不需要文件夹目录区分，都是解压后直接获得文件，则ZipEntry的名称就是文件名称即可；
                 * 2、如果zip文件解压后要有层级(目录)区分，那么只需要在ZipEntry的名称上，在文件名称前面将目录层级加上即可
                 * 如下就是"++i+'\\'+文件名称"，++i就是目录～目录和文件之间通过\\或者/进行分隔，目录就会自动创建
                 * 这样打包下载的时候ZipStream就会自动生成文件所在文件夹目录
                 * 3、zipOutputStream通过putNextEntry()设置完下一个文件的具体信息后，就要执行zipOutputStream.write()方法进行文件的写入操作
                 * zipOutputStream就是通过如上这种方式将压缩包中的每个文件所在目录与文件本身进行绑定的。
                 * **/
                /*ZipEntry zipEntry = new ZipEntry(file.getName());*/
                ZipEntry zipEntry = new ZipEntry(++i + "\\" + file.getName());
                /** 执行完zipOutputStream.putNextEntry后就需要执行zipOutputStream.write：相当于先声明要插入一个文件，然后就把文件写进去 **/
                zipOutputStream.putNextEntry(zipEntry);
                InputStream inputStream = new FileInputStream(file);
                // 缓冲区
                byte[] buffer = new byte[1024];
                // 读取文件流长度
                int len;
                // 读取到的文件内容没有结束，则写入输出流中
                while ((len = inputStream.read(buffer)) > 0) {
                    // 将读取到的文件信息写入输出流，从0开始，读取到最后一位。
                    // 不能省略off和len参数，因为如果文件结尾不够1024个字节那么outputStream.write(buffer)方法也会写入1024个字节，会导致文件信息丢失或被覆盖的问题
                    zipOutputStream.write(buffer, 0, len);
                }
                // 文件读取完关闭文件输入流
                inputStream.close();
            }
            /** 4、最终生成zip文件必须关闭文件流信息*/
            // 必须有下面2个关闭方法，否则zip导出的文件是无法打开的(因为用了try-with-resource，所以close方法不需要显式调用)
            // zipOutputStream.closeEntry();关闭后不影响下载，但是最好保留
            zipOutputStream.closeEntry();
            // zipOutputStream.close();
        } catch (Exception e) {
            log.error("打包下载异常：", e);
        }
    }

    /**
     * 打包下载(zip包)，兼容对目录的下载(此方法既可以传入文件进行打包下载，也可以传入一个目录(包含子目录，且空目录也可以选择是否创建)进行递归打包下载)
     * 在对文件下载的基础上使用递归找到目录下的所有文件并压缩打包
     * 参考：https://blog.csdn.net/qq_42582773/article/details/121755411
     *
     * @param sourceFolderPath 请求参数：主要为要打包的目标源文件的目录
     * @param response         zip文件返回
     */
    @GetMapping("/batchDownloadPath")
    public void batchDownloadPath(@RequestParam("sourceFolderPath") String sourceFolderPath
            , HttpServletResponse response) throws IOException {
        /** 文件输出定义*/
        String zipFileName = "脚手架工程.zip";
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(zipFileName, "UTF-8"));
        response.setContentType("application/octet-stream;charset=UTF-8");
        try (
                /** 2、得到ZipOutputStream用于生成zip文件*/
                // 将文件输出到指定位置还是直接输出到response的输出流根据业务需要决定选择即可～
                /*// 最终的压缩文件输出到指定目录
                OutputStream zipFileOutputStream = new FileOutputStream("/Users/mlamp/wangpeng/temp/压缩包文件.zip");*/
                // 最终的压缩文件输出到resonse输出流
                OutputStream outputStream = response.getOutputStream();
                // 获得zip输出流
                ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)
        ) {
            // 如果需要创建统一根目录，在此处传入根目录路径即可，如下图，将sourceFolderPath中的内容都打包到parentFolder\wp-initializer-folder\目录下
            /*compress("parentFolder\\wp-initializer-folder\\", new File(sourceFolderPath), zipOutputStream);*/
            compress("", new File(sourceFolderPath), zipOutputStream);
        } catch (Exception exception) {
            log.error("打包下载异常：", exception);
        }
    }

    /**
     * 对文件夹打包的核心逻辑
     *
     * @param parentFolderPath 父级目录(当前文件或文件夹所属目录)。打包时，当前文件夹或文件(currentFile)所在的目录。
     * @param currentFile      当前的目录或文件
     * @param zipOutputStream  输出流，用于打包
     */
    private void compress(String parentFolderPath, File currentFile, ZipOutputStream zipOutputStream) throws IOException {
        if (currentFile.isFile()) {
            /** currentFile是文件**/
            // file是文件
            ZipEntry zipEntry = new ZipEntry(parentFolderPath);
            zipOutputStream.putNextEntry(zipEntry);
            try (
                    InputStream inputStream = new FileInputStream(currentFile);
            ) {
                // 缓冲区
                byte[] buffer = new byte[1024];
                // 读取文件流长度
                int len;
                // 读取到的文件内容没有结束，则写入输出流中
                while ((len = inputStream.read(buffer)) > 0) {
                    // 将读取到的文件信息写入输出流，从0开始，读取到最后一位。
                    // 不能省略off和len参数，因为如果文件结尾不够1024个字节那么outputStream.write(buffer)方法也会写入1024个字节，会导致文件信息丢失或被覆盖的问题
                    zipOutputStream.write(buffer, 0, len);
                }
            } catch (Exception exception) {
                log.error("文件读取流异常：", exception);
            }
        } else {
            /** currentFile是目录**/
            /** 遍历当前目录下的所有文件以及文件夹，进行打包处理 */
            File[] files = currentFile.listFiles();
            if (files == null || files.length == 0) {
                /** 1、当前目录为空**/
                // 当前目录为空：创建该目录即可(空目录)。如果要舍弃空目录则注释该行代码即可
                zipOutputStream.putNextEntry(new ZipEntry(parentFolderPath + "\\"));
            } else {
                /** 2、当前目录不为空**/
                for (File file : files) {
                    // 递归处理
                    compress(parentFolderPath + "\\" + file.getName(), file, zipOutputStream);
                }
            }
        }
    }

    /**
     * 删除文件或目录
     *
     * @param path 文件路径或目录路径，如果是目录则递归删除子文件夹和其中的文件
     * @throws IOException
     */
    @GetMapping("/removeFileOrDirectory")
    public void removeFileOrDirectory(String path) throws IOException {
        FileUtil.removeFile(path);
    }

    /** 通过base64处理文件，即将文件转成base64字符串，以及将base64字符串还原成文件的方法参见TestBase64Controller中的方法 **/
}


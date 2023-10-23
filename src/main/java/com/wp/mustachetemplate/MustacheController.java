package com.wp.mustachetemplate;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wp.feign.feignapi.FileServiceApi;
import com.wp.feign.feignapi.TestServerApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description
 * @Author wangpeng
 * @Date 2023/10/17 17:25
 */
@Slf4j
@RestController
@RequestMapping("/api/mustache")
public class MustacheController {
    @Resource
    private FileServiceApi fileServiceApi;
    @Resource
    private TestServerApi testServerApi;

    @GetMapping("/demo")
    public void demo() throws IOException {
        MustacheFactory mustacheFactory = new DefaultMustacheFactory();
        Mustache mustache = mustacheFactory.compile("templates/TestMustacheController.java.mustache");

        Map<String, String> params = Maps.newHashMap();
        params.put("basePackage", "com.wp");
        StringWriter stringWriter = new StringWriter();

        mustache.execute(stringWriter, params).flush();
        System.out.println(stringWriter.toString());

    }

    /**
     * OutputStream和OutputStreamWriter是Java编程语言中的两个不同的类，用于处理输出流。
     * OutputStream是一个抽象类，它是所有输出流类的基类。它提供了一系列用于写入字节的方法，用于将字节写入到目标设备或文件中。
     * OutputStreamWriter是一个字符流类，它是OutputStream的子类。它将字符转换为字节，并将字节写入到指定的输出流中。它提供了一系列用于写入字符的方法，并且可以指定字符编码。
     * 简而言之，OutputStream用于写入字节数据(OutputStream.write(byte[], 0, len))，而OutputStreamWriter用于将字符数据转换为字节数据并写入输出流(OutputStreamWriter.write("成功"))。
     * 而且区别显而易见，一个直接写入内容，一个需要专程byte[]再写入
     * 通过使用OutputStreamWriter，我们还可以指定字符编码，以确保正确地将字符转换为字节。
     */
    @GetMapping("/createFile")
    public void createFile() throws IOException {
        // 1、创建读取.mustache模板的工厂类，并生成mustache工具对象。
        // 无参构造函数为默认从classpath中获取.mustache模板；可以传入resourceRoot指定模板文件位置
        MustacheFactory mustacheFactory = new DefaultMustacheFactory();
        Mustache mustache = mustacheFactory.compile("templates/TestMustacheController.java.mustache");

        // 2、创建填充模板的参数
        // 2.1、map的方式
        /*Map<String, String> params = Maps.newHashMap();
        params.put("basePackage", "com.wp");*/
        // 2.2、对象的方式
        MustacheParam params = new MustacheParam();
        params.setBasePackage("com.wp");

        // 3、指定写入后的输出
        OutputStreamWriter fileWriter = new FileWriter("/Users/mlamp/Desktop/TestMustacheController.java");

        // 4、执行写入
        mustache.execute(fileWriter, params);
        fileWriter.flush();

        // 5、关闭I/O
        fileWriter.close();
    }

    /**
     * for循环写文件
     * 场景：pom文件添加<dependency>
     *
     * @throws IOException
     */
    @GetMapping("/createFileWithForLoopAndWith")
    public void createFileWithForLoopAndWith() throws IOException {
        // 1、创建读取.mustache模板的工厂类，并生成mustache工具对象。
        // 无参构造函数为默认从classpath中获取.mustache模板；可以传入resourceRoot指定模板文件位置
        MustacheFactory mustacheFactory = new DefaultMustacheFactory();
        Mustache mustache = mustacheFactory.compile("templates/pom.xml.mustache");

        // 2、创建填充模板的参数
        MustacheParam params = new MustacheParam();
        List<Dependency> dependencies = Lists.newArrayList();
        Dependency dependency1 = new Dependency("org.springframework.boot", "spring-boot-starter-web", "2.1.17");
        Dependency dependency2 = new Dependency("com.google.guava", "guava", "18.0");
        Dependency dependency3 = new Dependency("org.projectlombok", "lombok", "0.6.11");
        dependencies.add(dependency1);
        dependencies.add(dependency2);
        dependencies.add(dependency3);
        params.setDependencies(dependencies);
        // 同if-else标签的操作
        params.setCondition(false);

        // 3、指定写入后的输出：可输出成任何类型文件～
        OutputStreamWriter fileWriter = new FileWriter("/Users/mlamp/Desktop/pom.xml");

        // 4、执行写入
        mustache.execute(fileWriter, params);
        fileWriter.flush();

        // 5、关闭I/O
        fileWriter.close();
    }

    /**
     * mock数据接口：接口支持幂等，文件或者目录已存在只会覆盖，不会出现重复等问题
     *
     * @param initialParams 生成组件的参数
     * @param response      响应信息，打包下载
     */
    @PostMapping("/mockRealInitializerProject")
    public void mockRealInitializerProject(@RequestParam("sourceFolderPath") String sourceFolderPath
            , @RequestBody @Valid List<InitializerProjectRequestParam> initialParams
            , HttpServletResponse response) throws IOException {
        // 创建读取.mustache模板的工厂类，并生成mustache工具对象。
        MustacheFactory mustacheFactory = new DefaultMustacheFactory("templates");
        Mustache mustache = null;
        /**
         * 1、遍历参数，逐个生成文件
         */
        for (InitializerProjectRequestParam template : initialParams) {
            // 1.1、获取模板
            mustache = mustacheFactory.compile(template.getMustacheTemplateName());
            // 1.2、获取模板参数
            Map<String, Object> params = template.getParams();
            // 1.3、指定写入后的输出：地址以及文件类型。
            // 目录不存在则手动创建目录
            File directory = new File(template.getDirectoryPath());
            if (!directory.exists()) {
                // 目录不存在则创建目录(包括子目录)
                directory.mkdirs();
            }
            // 如果文件是追加的方式写入，设置FileWriter的append属性为true即可
            OutputStreamWriter fileWriter = new FileWriter(template.getFilePath());
            // 1.4、执行写入
            mustache.execute(fileWriter, params);
            fileWriter.flush();
            // 1.5、关闭I/O
            fileWriter.close();
        }
        /**
         * 2、打zip包，需要递归目录进行打包操作～
         */
        /** 文件输出定义*/
//        String zipFileName = "脚手架工程.zip";
//        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(zipFileName, "UTF-8"));
//        response.setContentType("application/octet-stream;charset=UTF-8");
//        try (
//                /** 2、得到ZipOutputStream用于生成zip文件*/
//                // 将文件输出到指定位置还是直接输出到response的输出流根据业务需要决定选择即可～
//                // 最终的压缩文件输出到指定目录
//                OutputStream zipFileOutputStream = new FileOutputStream("/Users/mlamp/Desktop/wp-initialiazer.zip");
//                // 最终的压缩文件输出到resonse输出流
////                OutputStream outputStream = response.getOutputStream();
//                // 获得zip输出流
//                ZipOutputStream zipOutputStream = new ZipOutputStream(zipFileOutputStream)
//        ) {
//            compress("", new File(sourceFolderPath), zipOutputStream);
//        } catch (Exception exception) {
//            log.error("打包下载异常：", exception);
//        }
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
}

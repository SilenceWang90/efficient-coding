package com.wp.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wp.config.EasyExcelListener;
import com.wp.dto.ReadExcelDto;
import com.wp.dto.UserExportDto;
import com.wp.dto.UserFillData;
import com.wp.mapper.SaveMapper;
import com.wp.service.BusinessService;
import com.wp.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Classname FileServiceImpl
 * @Description 文件上传服务实现
 * @Date 2021/2/19 11:55
 * @Created by wangpeng116
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Resource
    private SaveMapper saveMapper;
    @Resource
    private BusinessService businessService;

    /**
     * 存储空间
     */
    private static final String BUCKET = "C:\\Users\\wangpeng116\\Desktop";
    private static final String BUCKET_MAC = "/Users/manman/Desktop";

    @Override
    public void upload(InputStream inputStream, String filename) {
        // 拼接文件存储路径
        String storagePath = BUCKET_MAC + "/" + filename;
        try (
                // JDK8 TWR不能关闭外部资源，因此定义内部流来实现对外部流的关闭
                InputStream innerInputStream = inputStream;
                FileOutputStream outputStream = new FileOutputStream(new File(storagePath))
        ) {
            // 缓冲区
            byte[] buffer = new byte[1024];
            // 读取文件流长度
            int len;
            // 读取到的文件内容没有结束，则写入输出流中
            while ((len = innerInputStream.read(buffer)) > 0) {
                // 将读取到的文件信息写入输出流，从0开始，读取到最后一位。
                // 不能省略off和len参数，因为如果文件结尾不够1024个字节那么outputStream.write(buffer)方法也会写入1024个字节，会导致文件信息丢失或被覆盖的问题
                outputStream.write(buffer, 0, len);
            }
            // 输出流的内容写入到文件（输出流的内容写入到磁盘）
            outputStream.flush();
        } catch (Exception e) {
            log.error("文件上传异常，文件名称：{}，异常信息：{}", filename, e);
        }
    }

    @Override
    public void upload(File file) {
        try {
            upload(new FileInputStream(file), file.getName());
        } catch (Exception e) {
            log.error("文件上传异常，文件名称：{}，异常信息：{}", file.getName(), e);
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String filePath = "/Users/manman/Desktop/wanlianyida/myoss_10808382.pdf";
        File file = new File(filePath);
        String filename = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.name());
        // import org.springframework.http.MediaTypeFactory;
        // 获取文件应该设置的contentType
        String contentType = MediaTypeFactory.getMediaType(filename)
                .orElse(MediaType.APPLICATION_OCTET_STREAM)
                .toString();
        System.out.println(contentType);
    }

    @Override
    public void download(String fileId, HttpServletResponse response) {
        //文件路径，一般是通过文件id获取，实际要根据实际的业务规则
        String filePath = "/Users/manman/Desktop/每日待办事项.txt";
        File file = new File(filePath);
        try (InputStream inputStream = new FileInputStream(file)) {
            // 输出流是从response中获取的，不需要手动关闭，也尽量不要关闭，否则可能影响拦截器的后置处理方法不执行
            OutputStream outputStream = response.getOutputStream();
            // 设置下载的文件名称(filename属性就是设置下载的文件名称叫什么，通过字符类型转换解决中文名称为空的问题)
            String filename = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.name());
            // 设置 Content-Disposition 响应头，告诉浏览器以下载方式处理该文件，并设置下载后的文件名。
            // 如果不设置此头，浏览器可能会尝试直接在窗口中打开文件（例如图片、PDF、文本等），或者下载的文件名可能不正确（如默认为接口名）。
            response.setHeader("content-disposition", "attachment;filename=" + filename);
            // 设置 Content-Type 响应头，告知浏览器文件内容的媒体类型。
            // application/octet-stream 是通用的二进制流类型。结合 Content-Disposition 使用，可以确保浏览器正确下载文件。
            /** 如果想准确获取文件的content-type，代码如下：
             * // import org.springframework.http.MediaTypeFactory;
             *         // 获取文件应该设置的contentType
             *         String contentType = MediaTypeFactory.getMediaType(filename)
             *                 .orElse(MediaType.APPLICATION_OCTET_STREAM)
             *                 .toString();
             *         System.out.println(contentType);
             * **/
            response.setContentType("application/octet-stream;charset=UTF-8");
            /** 输入流直接写到输出流，最棒的方式！节省内存，效率极高 **/
            /**
             * // Spring 提供的工具类，底层就是你写的那段 while 循环。IO关闭还是得自己做，推荐try-with-resource
             *  StreamUtils.copy(inputStream, outputStream);
             * // 或者 Apache Commons IO
             *  IOUtils.copy(inputStream, outputStream);
             * **/
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
        } catch (Exception e) {
            log.error("文件下载失败，文件路径：{}，异常信息：{}", filePath, e);
        }
    }

    @Override
    public void exportExcel(HttpServletResponse response) {
        try (
                OutputStream outputStream = response.getOutputStream()
        ) {
            String filename = new String("excel导出的文件.xlsx".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            response.setHeader("content-disposition", "attachment;filename=" + filename);
            // 1、创建EasyExcel导出对象，注明输出流以及对应的导出实体类型，在导出实体类型中增加easyexcel的注解功能(指明列以及样式等等)
            ExcelWriter excelWriter = EasyExcelFactory.write(outputStream, UserExportDto.class).build();
            // 2、分批加载数据：即分页查询出要导出的数据，通过循环修改分页查询参数然后查询出不同页码的数据
            // 示例为了方便手动造2条数据用于演示
            List<UserExportDto> list1 = Lists.newArrayList();
            List<UserExportDto> list2 = Lists.newArrayList();
            UserExportDto userExportDto1 = new UserExportDto();
            userExportDto1.setUserId(12L);
            userExportDto1.setUserName("wangpeng");
            userExportDto1.setAge(18);
            userExportDto1.setCreateTime(LocalDateTime.now());

            UserExportDto userExportDto2 = new UserExportDto();
            userExportDto2.setUserId(16L);
            userExportDto2.setUserName("yumanlu");
            userExportDto2.setAge(18);
            userExportDto2.setCreateTime(LocalDateTime.now());

            list1.add(userExportDto1);
            list2.add(userExportDto2);
            // 数据导出到哪个sheet页中，参数1是sheet页码，参数2是给sheet页起名
            WriteSheet writeSheet = EasyExcelFactory.writerSheet(0, "数据").build();
            // 3、导出分批加载的数据（实际业务场景就是分页查询的结果进行处理，这里用while循环来替代演示）
            for (int i = 0; i < 2; i++) {
                if (i == 1) {
                    excelWriter.write(list1, writeSheet);
                } else {
                    excelWriter.write(list2, writeSheet);
                }
            }
            // 关闭excelWriter
            excelWriter.finish();
            log.info("完成导出");
        } catch (Exception e) {
            log.error("文件导出失败，导出异常信息：{}", e);
        }
    }


    /**
     * 填充数据到固定模板(模板中通过{属性名}来确定要填充的内容)
     *
     * @param response
     */
    @Override
    public void exportWithFillDataInTemplate(HttpServletResponse response) throws UnsupportedEncodingException {
        // 1、获取模板
        String templateFilePath = "C:/Users/wangpeng116/Desktop/导出模板.xlsx";
        String filename = new String("导出模板.xlsx".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        response.setHeader("content-disposition", "attachment;filename=" + filename);
        response.setCharacterEncoding("UTF-8");
        // 2、查询数据
        List<UserFillData> list = Lists.newArrayList();
        UserFillData userExportDto1 = new UserFillData();
        userExportDto1.setUserId(12L);
        userExportDto1.setUserName("wangpeng");
        UserFillData userExportDto2 = new UserFillData();
        userExportDto2.setUserId(16L);
        userExportDto2.setUserName("yumanlu");
        list.add(userExportDto1);
        list.add(userExportDto2);
        // 3、创建EasyExcel对象
        try (
                OutputStream outputStream = response.getOutputStream()
        ) {
            ExcelWriter excelWriter = EasyExcelFactory.write(outputStream).withTemplate(templateFilePath).build();
            // 写到哪个sheet页上，切记sheetNo是从0开始而不是从1开始
            WriteSheet writeSheet = EasyExcelFactory.writerSheet(0, "数据1").build();
            excelWriter.fill(list, writeSheet);
            // map也可以，当前测试多个sheet页同时填充数据
            Map<String, String> map = Maps.newHashMap();
            map.put("testInfo", "211314");
            WriteSheet writeSheet2 = EasyExcelFactory.writerSheet(1, "数据2").build();
            excelWriter.fill(map, writeSheet2);
            //关闭
            excelWriter.finish();
            log.info("完成导出");
        } catch (Exception e) {
            log.error("异常信息：{}", e);
        }
    }

    /**
     * EasyExcel读取Excel
     *
     * @param inputStream
     */
    @Override
    public void readEasyExcel(InputStream inputStream) {
        // EasyExcelListener构造函数导入Spring的对象
        ExcelReader excelReader = EasyExcel.read(inputStream, ReadExcelDto.class, new EasyExcelListener(businessService, saveMapper)).build();
        // 读取哪个sheet
        ReadSheet readSheet = EasyExcel.readSheet(0)
//                .headRowNumber(0？-1？)官网来看这个值表示excel中头的行数，默认是1，所以如果是0感觉符合要求
                .build();
        // 设置从第几行开始读取数据？？默认是1即跳过首行，设置为0则从首行就开始读取数据？？？？？？
//        readSheet.setHeadRowNumber(0？-1？);官网来看这个值表示excel中头的行数，默认是1，所以如果是0感觉符合要求
        excelReader.read(readSheet);
        // 读取多个sheet
        /*ReadSheet readSheet2 = EasyExcel.readSheet(1).build();
        excelReader.read(readSheet, readSheet2);*/
        // 关闭流
        excelReader.finish();
    }

    /**
     * 异步导出
     *
     * @param response
     */
    @Async(value = "exportServiceExecutor")
    @Override
    public void asyncExport(HttpServletResponse response) {
        try {
            String filename = new String("excel导出的文件.xlsx".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            response.setHeader("content-disposition", "attachment;filename=" + filename);

            List<UserExportDto> list = Lists.newArrayList();
            UserExportDto userExportDto1 = new UserExportDto();
            userExportDto1.setUserId(12L);
            userExportDto1.setUserName("wangpeng");
            userExportDto1.setAge(18);
            userExportDto1.setCreateTime(LocalDateTime.now());
            list.add(userExportDto1);
            UserExportDto userExportDto2 = new UserExportDto();
            userExportDto2.setUserId(16L);
            userExportDto2.setUserName("yumanlu");
            userExportDto2.setAge(18);
            userExportDto2.setCreateTime(LocalDateTime.now());
            list.add(userExportDto2);
            // 异步导出只能这么写，因为异步时主线程已关闭response.getOutputStream()，子线程无法关闭输出流就会报错
            EasyExcel.write("文件路径", UserExportDto.class).sheet(0).doWrite(list);
            log.info("完成导出");
        } catch (Exception e) {
            log.error("文件导出失败，导出异常信息：{}", e);
        }
    }
}

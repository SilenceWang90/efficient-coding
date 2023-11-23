package com.wp.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description 文件处理工具类
 * @Author wangpeng
 * @Date 2023/10/30 16:12
 */
@Slf4j
public class FileUtil {
    private FileUtil() {
    }

    /**
     * 将sourceFile打包成zip，并通过outputStream导出
     *
     * @param outputStream 输出流，zip文件产出的位置
     * @param sourceFile   待打包的资源，可以是文件可以是目录
     */
    public static void zip(OutputStream outputStream, String sourceFile) {
        try (
                // 获得zip输出流
                ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)
        ) {
            compress("", new File(sourceFile), zipOutputStream);
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
    private static void compress(String parentFolderPath, File currentFile, ZipOutputStream zipOutputStream) throws IOException {
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
     * 删除文件或目录(包括子目录及目录中的文件)
     * 传文件则删除文件，传目录则删除目录及其子目录
     *
     * @param filePath
     */
    public static void removeFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        // 遍历文件夹
        Files.walkFileTree(path,
                new SimpleFileVisitor<Path>() {
                    // 先去遍历删除文件
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        log.info("文件被删除，文件路径：{}", file);
                        return FileVisitResult.CONTINUE;
                    }

                    // 再去遍历删除目录
                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        log.info("文件夹被删除，文件路径：{}", dir);
                        return FileVisitResult.CONTINUE;
                    }
                }
        );
    }
}

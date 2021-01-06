package com.wp.lambda.file;

/**
 * @Classname FileConsumer
 * @Description 文件处理函数式接口
 * @Date 2021/1/6 16:59
 * @Created by wangpeng116
 */
@FunctionalInterface
public interface FileConsumer {

    /**
     * 函数式接口抽象方法
     *
     * @param fileContent 文件内容
     */
    void fileHandler(String fileContent);
}

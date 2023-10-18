package com.wp.mustachetemplate;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author wangpeng
 * @Date 2023/10/17 17:25
 */
@RestController
@RequestMapping("/api/mustache")
public class MustacheController {
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
     * mock数据接口
     *
     * @param initialParams 生成组件的参数
     * @param response      响应信息，打包下载
     */
    @PostMapping("/mockRealInitializerProject")
    public void mockRealInitializerProject(@RequestBody @Valid List<InitializerProjectRequestParam> initialParams
            , HttpServletResponse response) throws IOException {
        // 创建读取.mustache模板的工厂类，并生成mustache工具对象。
        MustacheFactory mustacheFactory = new DefaultMustacheFactory();
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
            // 其中application.properties文件是追加的方式写入
            OutputStreamWriter fileWriter = new FileWriter(template.getOutputUri(), true);
            // 1.4、执行写入
            mustache.execute(fileWriter, params);
            fileWriter.flush();
            // 1.5、关闭I/O
            fileWriter.close();
        }
        /**
         * 2、todo：打zip包，需要递归目录进行打包操作～待验证
         */


    }

}

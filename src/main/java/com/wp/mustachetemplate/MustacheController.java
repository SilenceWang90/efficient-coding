package com.wp.mustachetemplate;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.StringWriter;
import java.util.Map;

/**
 * @Description
 * @Author wangpeng
 * @Date 2023/10/17 17:25
 */
@RestController
@RequestMapping("/api/mustache")
public class MustacheController {
    @GetMapping("/getCreateFile")
    public void createFile(){
        MustacheFactory mustacheFactory = new DefaultMustacheFactory();
        Mustache mustache = mustacheFactory.compile("TestMustacheController.java.mustache");

        Map<String,String> params = Maps.newHashMap();
        params.put("basePackage","com.wp");
        StringWriter stringWriter = new StringWriter();

        mustache.execute(stringWriter,params);
        System.out.println(stringWriter.toString());

    }
}

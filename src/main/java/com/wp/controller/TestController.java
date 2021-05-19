package com.wp.controller;

import com.wp.dto.LinkObjectDto;
import com.wp.dto.UserFillData;
import com.wp.service.RetryableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Classname TestController
 * @Description 测试接口
 * @Date 2021/3/17 10:55
 * @Created by wangpeng116
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private RetryableService retryableService;
    @Autowired
    private List<HttpMessageConverter> converters;

    @GetMapping("/getLinkObjectDtoInfo")
    public LinkObjectDto getLinkObjectDtoInfo() {
        LinkObjectDto head = new LinkObjectDto();
        head.setValue("我是head");
        LinkObjectDto node1 = new LinkObjectDto();
        node1.setValue("我是node1");
        LinkObjectDto node2 = new LinkObjectDto();
        node2.setValue("我是node2");
        LinkObjectDto node3 = new LinkObjectDto();
        node3.setValue("我是node3");
        LinkObjectDto node4 = new LinkObjectDto();
        node4.setValue("我是node4");

        head.setLinkObjectDto(node1);
        node1.setLinkObjectDto(node2);
        node2.setLinkObjectDto(node3);
        node3.setLinkObjectDto(node4);
        return head;
    }

    @GetMapping("/getLinkObjectDtoInfoFromInterface")
    public LinkObjectDto getLinkObjectDtoInfoFromInterface() {
        LinkObjectDto head = restTemplate.getForObject("http://localhost:8080/test/getLinkObjectDtoInfo", LinkObjectDto.class);
        String result = "";
        LinkObjectDto current = head;
        while (current != null) {
            result = String.join(",", result, current.getValue());
            current = current.getLinkObjectDto();
        }
        System.out.println(result);
        System.out.println("===============================");
        String calcResult = this.getResult(head);
        System.out.println("递归结果：" + calcResult);
        return head;
    }

    public String getResult(LinkObjectDto linkObjectDto) {
        // 1、结束条件：子逻辑不存在
        if (linkObjectDto == null) {
            return "";
        }
        // 2、逻辑处理
        String result = linkObjectDto.getValue();
        // 3、递归公式(当前方法如何使用递归的结果)：当前结果+递归结果
        result = result + getResult(linkObjectDto.getLinkObjectDto());
        return result;
    }

    @GetMapping("/test2")
    public void test2(){
        System.out.println(fib(1));
        System.out.println(fib(2));
        System.out.println(fib(3));
        System.out.println(fib(4));
        System.out.println(fib(5));
    }

    public int fib(int n) {
        if (n == 1) {
            return 1;
        } else if (n == 2) {
            return 1;
        } else {
            return fib(n - 1) + fib(n - 2);
        }
    }

    @GetMapping("/testRetry")
    public String testRetry(){
        String result = retryableService.testRetry("成功");
        return result;
    }

    @GetMapping("/testRetryRecover")
    public String testRetryRecover(){
        retryableService.testRecover("成功");
        return "成功";
    }

    @GetMapping("/getUserFillData")
    public UserFillData getUserFillData(@RequestBody UserFillData userFillData){
        UserFillData userFillData1 = new UserFillData();
        userFillData1.setUserId(21321424L);
        userFillData1.setUserName("wangpeng");
        userFillData1.setCreateDate(new Date());
        return userFillData1;
    }

    @GetMapping("/test1")
    public void test1() {
        log.info("所有消息转换器：{}", converters);
    }

}

package com.wp.controller;

import com.wp.dto.LinkObjectDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @Classname TestController
 * @Description 测试接口
 * @Date 2021/3/17 10:55
 * @Created by wangpeng116
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    private RestTemplate restTemplate;

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
        return head;
    }


}

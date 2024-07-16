package com.wp.session;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * session创建和使用过程：session是服务器生成，服务器保留了session对象以及该session对象的id的映射关系，并将session的id返回给客户端。
 * 这样下次请求的时候客户端带着session的id，服务器就能从维护的session对象和sessionid的映射关系中查找到之前生成的session对象。
 * 1、创建session对象：
 * 1.1、浏览器发起请求至tomcat服务器，tomcat从请求中的cookie中没有找到JSESSIONID这个key的cookie信息（JSESSIONID这个key
 * 存储的对应的value是tomcat生成的session对象的id）。tomcat会生成一个session对象以及该对象对应的sessionid。
 * 1.2、tomcat将该session存储在当前的tomcat服务器，并存储当前session的id。并将创建session对象赋值到springmvc的HttpSession/HttpServletRequest的session对象中。
 * 这样springmvc的接口就能通过HttpSession或HttpServletRequest获取到该session对象。
 * 1.3、在请求返回的时候tomcat将创建的session对象的sessionid存储在response的cookie中，这样浏览器就能获取到一个key为JSESSIONID的cookie，value就是当前session对象的id。
 * <p>
 * 2、获取session对象：
 * 2.1、浏览器发起请求至tomcat服务器，tomcat从请求中的cookie中获取到JSESSIONID这个key的value(即tomcat服务器创建session时对应的id)对应的session对象。
 * 2.2、将其复制给springmvc的HttpSession/HttpServletRequest的session对象中，这样springmvc的接口就能通过HttpSession或HttpServletRequest获取到该session对象。
 *
 * @author wangpeng
 * @description TestSessionController
 * @date 2024/7/15 15:31
 **/
@RestController
@RequestMapping("/testSession")
public class TestSessionController {

    /**
     * 通过HttpServletRequest获取Session并向Session对象赋值
     *
     * @param request
     * @return
     */
    @GetMapping("/set")
    public String setSession(HttpServletRequest request) {
        // 通过HttpServletRequest获取Session对象
        HttpSession session = request.getSession();
        // 向Session中存储一个键值对
        session.setAttribute("username", "user123");
        return "Session data set";
    }

    /**
     * 通过HttpServletRequest获取Session并从Session对象获取数据
     *
     * @param request
     * @return
     */
    @GetMapping("/get")
    public String getSession(HttpServletRequest request) {
        // 通过HttpServletRequest获取Session对象
        HttpSession session = request.getSession();
        // 从Session中获取值
        String username = (String) session.getAttribute("username");
        return "Username from session: " + username;
    }
}

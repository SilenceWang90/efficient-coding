package com.wp.i18n;

import com.google.common.collect.Maps;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @Description
 * @Author admin
 * @Date 2024/6/23 11:47
 */
@Controller
@RequestMapping("/test-i18n")
public class TestI18nController {

    @Resource
    private MessageSource messageSource;

    @GetMapping("/testI18nHtml")
    public String testI18nHtml(HttpServletRequest request, Locale locale) {
        // 方式1：不推荐，因为可能lang的格式是语言_地区，这样的话Locale.forLanguageTag就无法加载正确的Locale，而是获取默认的Locale
        String greet1 = messageSource.getMessage("greet", null, Locale.forLanguageTag(request.getParameter("lang")));
        // 方式2：推荐，获取当前LocaleResolver解析后的locale，不过在Controller中获取方便
        String greet2 = messageSource.getMessage("greet", null, locale);
        // 方式3：强烈推荐，在任何bean中都能通过LocaleContextHolder.getLocale()获取当前的Locale
        String greet3 = messageSource.getMessage("greet", null, LocaleContextHolder.getLocale());
        System.out.println(greet1);
        System.out.println(greet2);
        System.out.println(greet3);
        System.out.println(messageSource.getMessage("say", new Object[]{"爸爸"}, LocaleContextHolder.getLocale()));
        return "i18n-test";
    }

    /**
     * 指定需要的key，返回对应的key的信息
     *
     * @param specificKeys 指定的key
     * @param locale       springmvc解析的locale
     * @return
     */
    @GetMapping("/getSpecificI18nInfo")
    @ResponseBody
    public Map<String, Object> getSpecificI18nInfo(@RequestParam("specific-keys") List<String> specificKeys, Locale locale) {
        Map<String, Object> info = Maps.newHashMap();
        specificKeys.forEach(obj -> info.put(obj, messageSource.getMessage(obj, null, locale)));
        return info;
    }

    /**
     * 读取配置文件，一次性加载所有的内容
     * @param lang
     * @param locale
     * @return
     */
//    @GetMapping("/getSpecificI18nInfo")
//    @ResponseBody
//    public Map<String, Object> getSpecificI18nInfo(@RequestParam("lang") String lang, Locale locale) {
//        messageSource.
//        return info;
//    }
}

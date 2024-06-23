package com.wp.i18n;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @Description
 * @Author admin
 * @Date 2024/6/23 12:04
 */
@Component(value = "localeResolver")
@Slf4j
public class CustomLocaleResolver implements LocaleResolver {
    /**
     * 解析request，创建国际化需要的Locale给MessageSource
     * 下面暂时只有根据请求url参数获取lang去解析Locale
     *
     * @param request
     * @return
     */
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String lang = request.getParameter("lang");
        Locale result;
        if (StringUtils.isNotBlank(lang)) {
            // 兼容两种格式，如英文的：en、en_US，两种格式不同但都是国际标准
            String[] parts = lang.split("_");
            if (parts.length == 2) {
                result = new Locale(parts[0], parts[1]);
            } else {
                result = Locale.forLanguageTag(lang);
            }

        } else {
            result = Locale.getDefault();
        }
        return result;
    }

    /**
     * 增加断点，打日志确认该方法不会被调用，暂不知具体作用
     *
     * @param request
     * @param response
     * @param locale
     */
    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
    }

}

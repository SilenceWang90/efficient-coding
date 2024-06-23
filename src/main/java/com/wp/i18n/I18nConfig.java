package com.wp.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @Description
 * @Author admin
 * @Date 2024/6/23 11:34
 */
@Configuration
public class I18nConfig {
    /**
     * 创建messageSource，指定国际化文件的path和编码格式
     *
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        // 指定要读取的国际化的资源文件
        messageSource.setBasenames("classpath:static/i18n/wp_test_html", "classpath:static/i18n/wp_test_js");
        // 指定编码格式防止乱码
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}

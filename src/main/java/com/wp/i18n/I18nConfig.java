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
        // 指定要读取的国际化的资源文件。注意填写的是basename的路径，即在idea中创建Resource Bundle设置的basename～～～不需要把文件的全名称写上
        /**1、ReloadableResourceBundleMessageSource：路径必须是带classpath:或者file:的，因为ReloadableResourceBundleMessageSource不仅可以
         * 从classpath中加载文件，也可以从file文件系统中查找，所以如果不写classpath:或者file:则无法读取资源文件**/
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:static/i18n/wp_test_html", "classpath:static/i18n/wp_test_js");
        //messageSource.addBasenames("file:/Users/manman/wangpeng/static/i18n/wp_test_html"
        //        , "file:/Users/manman/wangpeng/static/i18n/wp_test_js");
        /**2、ResourceBundleMessageSource：路径必须是没有classpath的，因为ResourceBundleMessageSource默认就是从classpath中查找，
         * 再写classpath:就找不到了～～～**/
        /*ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("static/i18n/wp_test_html", "static/i18n/wp_test_js");
        messageSource.addBasenames("static/i18n/wp_test_html", "static/i18n/wp_test_js");*/
        /** ReloadableResourceBundleMessageSource和ResourceBundleMessageSource二者的异同 **/
        // 1、setBaseNames()和addBaseNames()方法的处理方式相同：setBaseNames()就是重置当前的baseName为当前设置的，即当前的覆盖之前设置的，所以多个setBaseNames()以最后一个加载的为准；
        // addBaseNames()是追加，不会覆盖之前的。
        // 2、ReloadableResourceBundleMessageSource可以通过设置CacheMillis实现不重启项目自动每隔多久重新加载资源文件，如果不设置则默认是-1即不主动重新加载；
        // ResourceBundleMessageSource就是常规的启动时加载，没有自动加载的功能。
        // 指定编码格式防止乱码
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}

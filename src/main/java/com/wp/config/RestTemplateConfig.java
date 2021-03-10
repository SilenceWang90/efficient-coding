package com.wp.config;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;


/**
 * @Classname RestTemplateConfig
 * @Description RestTemplate配置
 * @Date 2019/4/24 11:19
 * @Created by wangpeng116
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 高级的ClientHttpRequestFactory工厂配置，底层基于HttpClient
     * 可以配置httpClient链接池
     *
     * @return
     */
    @Bean("componetsClientHttpRequestFactory")
    public ClientHttpRequestFactory componetsClientHttpRequestFactory(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        //获取连接超时时间
        factory.setConnectTimeout(6000);
        //获取数据超时时间
        factory.setReadTimeout(30000);
        factory.setHttpClient(httpClient);
        return factory;
    }

    //创建链接池管理器
    @Bean
    public HttpClientConnectionManager httpClientConnectionManager() {
        PoolingHttpClientConnectionManager poolConnectionManager = new PoolingHttpClientConnectionManager();
        //链接池最大连接数
        poolConnectionManager.setMaxTotal(5);
        //闲置连接超时回收
        poolConnectionManager.closeIdleConnections(60, TimeUnit.SECONDS);
        //每台服务器默认最大连接数
        poolConnectionManager.setDefaultMaxPerRoute(3);
        //获取链接池链接，链接不活跃多久后，验证链接是否可用。
        poolConnectionManager.setValidateAfterInactivity(1000);
        return poolConnectionManager;
    }

    //配置HttpClientBuilder，用于创建httpClient
    //可以配置SSL安全证书信息等。
    @Bean
    public HttpClientBuilder httpClientBuilder(HttpClientConnectionManager httpClientConnectionManager) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        //注入连接工厂配置
        httpClientBuilder.setConnectionManager(httpClientConnectionManager);
        //闲置链接回收
        httpClientBuilder.evictIdleConnections(2000, TimeUnit.MILLISECONDS);
        return httpClientBuilder;
    }

    //创建httpClient
    @Bean
    public HttpClient httpClient(HttpClientBuilder httpClientBuilder) {
        //创建httpClient对象。
        HttpClient httpClient = httpClientBuilder.build();
        return httpClient;
    }


    /**
     * restTemplate配置
     *
     * @param clientHttpRequestFactory
     * @return
     */
    @Bean
    public RestTemplate restTemplate(@Qualifier("componetsClientHttpRequestFactory") ClientHttpRequestFactory clientHttpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        return restTemplate;
    }


}

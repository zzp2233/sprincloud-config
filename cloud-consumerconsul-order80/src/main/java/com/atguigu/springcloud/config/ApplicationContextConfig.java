package com.atguigu.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @auther zzyy
 * @create 2020-02-19 15:20
 */
@Configuration
public class ApplicationContextConfig
{
    @Bean
    @LoadBalanced //负载均衡，将请求的地址中的服务逻辑名转为具体的服务地址
    public RestTemplate getRestTemplate()
    {
        return new RestTemplate();
    }
}

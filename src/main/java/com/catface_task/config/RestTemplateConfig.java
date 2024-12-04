package com.catface_task.config;


import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @className: RestTemplateConfig
 * @author: Havoc.
 * @date: 2024/12/2 01:26
 * @version: 0.1
 * @description:
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 没有实例化RestTemplate时，初始化RestTemplate
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

}

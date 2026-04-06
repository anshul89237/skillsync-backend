package com.lpu.MentorService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

@Configuration
public class FeignInternalRequestConfig {

    @Bean
    public RequestInterceptor internalRequestHeaderInterceptor() {
        return template -> template.header("X-Internal-Request", "gateway");
    }
}

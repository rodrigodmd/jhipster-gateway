package com.rodrigodmd.core.gateway.config;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.rodrigodmd.core.gateway")
public class FeignConfiguration {

}

package com.apigaworks.algafood.common.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration //estou usando o CorsConfig
public class WebConfig implements WebMvcConfigurer {

//    vou usar apenas o
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // isso habilita tudo
//                .allowedOrigins("http://localhost:3000") // isso ta redundante, tem outros metodos pq é usado em preflith
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT");
//
//    }
}
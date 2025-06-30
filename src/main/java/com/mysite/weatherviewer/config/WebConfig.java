package com.mysite.weatherviewer.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.mysite.weatherviewer.controller")
public class WebConfig implements WebMvcConfigurer {
    // настройка Thymeleaf ViewResolver, ресурсных хендлеров и т.д.
}

package com.mysite.weatherviewer.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

// **минимально необходимый класс для подключения Spring MVC без Spring Boot**
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
//        return new Class[]{AppConfig.class}; // Hibernate, DataSource, сервисы
        return null; // Hibernate, DataSource, сервисы
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class}; // Spring MVC, ViewResolver, Thymeleaf
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"}; // на какие URL вешаем DispatcherServlet
    }
}
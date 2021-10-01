package com.dong.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Andong Zhang
 * @create 2021-08-05-0:28
 */
@Configuration
public class MySpringMvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/pay/success").setViewName("success");

    }
}

package com.forum.forum.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan
public class WebConfig implements WebMvcConfigurer {

    public static String uploadDirectory= System.getProperty("user.home") + "\\forumImages";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/forumImages/**", "/css/**", "/img/**", "/js/**")
                .addResourceLocations("file:" + uploadDirectory+"\\", "classpath:/static/css/", "classpath:/static/img/", "classpath:/static/js/");
    }

}

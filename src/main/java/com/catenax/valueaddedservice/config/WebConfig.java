package com.catenax.valueaddedservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Value("${vas.datasource.host}")
    private String host;

    @Value("${vas.datasource.pass}")
    private String password;

    @Value("${vas.datasource.user}")
    private String user;

    @Value("${application.name}")
    private String applicationName;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        log.info("user {} pass {} host {} applicationName {}",user,password,host,applicationName);
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");

    }
}

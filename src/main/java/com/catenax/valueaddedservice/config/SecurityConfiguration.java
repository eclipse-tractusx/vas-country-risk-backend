package com.catenax.valueaddedservice.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Security config bean
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration  {


    @Bean
    @ConditionalOnProperty(prefix = "security", name = "enabled", havingValue = "true")
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {


        httpSecurity.cors().and().csrf().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/**")
                .authenticated()
                .and().
                oauth2ResourceServer().jwt();

        return httpSecurity.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*");
            }
        };
    }

    @Bean
    @ConditionalOnProperty(prefix = "security", name = "enabled", havingValue = "false")
    public SecurityFilterChain securityFilterChainLocal(final HttpSecurity httpSecurity) throws Exception {

        httpSecurity.httpBasic().disable();
        httpSecurity.formLogin().disable();
        httpSecurity.logout().disable();
        httpSecurity.headers().frameOptions().disable();

        httpSecurity.cors().and().csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/**")
                .permitAll();

        return httpSecurity.build();
    }


}
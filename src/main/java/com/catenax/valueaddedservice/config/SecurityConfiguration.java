package com.catenax.valueaddedservice.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Security config bean
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private static final String[] WHITELIST  = {
            "/h2/**",
            "/api/swagger-ui/**",
            "/api/api-docs",
            "/api/api-docs.yaml",
            "/api/api-docs/swagger-config",
    };

    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    @Bean
    @ConditionalOnProperty(prefix = "security", name = "enabled", havingValue = "true")
        public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic().disable();
        httpSecurity.formLogin().disable();
        httpSecurity.csrf().disable();
        httpSecurity.logout().disable();
        httpSecurity.cors().disable();
        httpSecurity.headers().frameOptions().disable();

        httpSecurity
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(WHITELIST)
                .permitAll()
                .antMatchers("/api/**")
                .authenticated()
                .and().
                oauth2ResourceServer().jwt();

        return httpSecurity.build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "security", name = "enabled", havingValue = "false")
    public SecurityFilterChain securityFilterChainLocal(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic().disable();
        httpSecurity.formLogin().disable();
        httpSecurity.csrf().disable();
        httpSecurity.logout().disable();
        httpSecurity.cors().disable();
        httpSecurity.headers().frameOptions().disable();

        httpSecurity
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(WHITELIST)
                .permitAll()
                .antMatchers("/api/**")
                .permitAll();

        return httpSecurity.build();
    }

    @Bean
        /* package */ CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("Access-Control-Allow-Headers", "Access-Control-Allow-Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers", "Origin", "Cache-Control", "Content-Type", "Authorization"));
        configuration.setAllowedMethods(List.of("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH", "OPTIONS"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
/********************************************************************************
 * Copyright (c) 2022,2023 BMW Group AG
 * Copyright (c) 2022,2023 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/
package org.eclipse.tractusx.valueaddedservice.config;

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
                .authorizeHttpRequests()
                .requestMatchers("/error","/api/dashboard/**","/api/sharing/**","/api/edc/**")
                .authenticated()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**","/management/**")
                .permitAll()
                .and()
                .oauth2ResourceServer().jwt();

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

        httpSecurity.cors().and().csrf().ignoringRequestMatchers("/api/**").and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/error","/api/**","/management/**","/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**")
                .permitAll();

        return httpSecurity.build();
    }


}
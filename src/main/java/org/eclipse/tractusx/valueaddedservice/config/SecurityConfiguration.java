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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Security config bean
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration  {


    @Bean
    @ConditionalOnProperty(prefix = "security", name = "enabled", havingValue = "true")
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {

        httpSecurity.cors(withDefaults())
                        .csrf(((csrf)-> csrf.disable()))
                                .authorizeHttpRequests(((authz)-> authz
                                        .requestMatchers("/error","/api/dashboard/**","/api/sharing/**","/api/edc/**")
                                        .authenticated()
                                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**","/management/**")
                                        .permitAll()
                                ));

        httpSecurity.oauth2ResourceServer(resourceServer -> resourceServer
                .jwt(withDefaults()));


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



        httpSecurity.cors(withDefaults())
                .csrf(((csrf)-> csrf.disable()))
                .formLogin(((form)-> form.disable()))
                .httpBasic((httpBasic)-> httpBasic.disable())
                .logout((logout)-> logout.disable())
                .headers((headers)->headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))
                .authorizeHttpRequests(((authz)-> authz
                        .requestMatchers("/error","/api/**","/management/**","/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**")
                        .permitAll()
                ));



        return httpSecurity.build();
    }


}
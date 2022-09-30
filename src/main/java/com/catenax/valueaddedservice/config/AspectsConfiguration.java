package com.catenax.valueaddedservice.config;


import com.catenax.valueaddedservice.interceptors.TokenAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;


@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AspectsConfiguration {


    @Bean
    public TokenAspect environmentAspect(Environment env) {
    	return new TokenAspect(env);
    }
    
}

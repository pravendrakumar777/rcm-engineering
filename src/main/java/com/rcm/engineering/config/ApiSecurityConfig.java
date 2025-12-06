package com.rcm.engineering.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(1)
public class ApiSecurityConfig {

    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                //.antMatcher("/api/**")
                //.antMatcher("/attendance/**")
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().permitAll();
        return http.build();
    }
}

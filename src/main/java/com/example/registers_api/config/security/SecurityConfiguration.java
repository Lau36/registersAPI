package com.example.registers_api.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity

public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html", "/swagger-ui/index.html").permitAll()
                        .requestMatchers(HttpMethod.POST,"/Test").permitAll()
                        .requestMatchers(HttpMethod.POST,"/ResearchLayer").permitAll()
                        .requestMatchers(HttpMethod.GET,"/ResearchLayer/GetAll").permitAll()
                        .requestMatchers(HttpMethod.GET,"/ResearchLayer").permitAll()
                        .requestMatchers(HttpMethod.POST,"/Variable").permitAll()
                        .requestMatchers(HttpMethod.GET,"/Variable/GetAll").permitAll()
                        .requestMatchers(HttpMethod.GET,"/Variable/ResearchLayerId").permitAll()
                        .requestMatchers(HttpMethod.GET,"/Variable").permitAll()

                        .anyRequest().authenticated()
                )
                .build();
    }
}
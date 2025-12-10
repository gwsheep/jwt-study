package com.jstudy.jwtandresource.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(
                    auth -> auth.requestMatchers(("/verify")).permitAll()
                            .anyRequest().authenticated())
            //oauth2 설정?
            .oauth2ResourceServer(o -> o.jwt(
                    jwt -> {//jwt 검증
                    }
            ))
            .httpBasic(Customizer.withDefaults());

        return http.build();

    }




}

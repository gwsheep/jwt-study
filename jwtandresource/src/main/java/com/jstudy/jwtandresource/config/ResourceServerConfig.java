package com.jstudy.jwtandresource.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

//Jwt 검증 설정
@Configuration
public class ResourceServerConfig {

    @Value("${jwt.publicKey}")
    private String publicKey;


    




}

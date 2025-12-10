package com.jstudy.jwtandresource.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

//Jwt 검증 설정
@Configuration
public class ResourceServerConfig {

    @Value("${jwt.publicKey}")
    private String publicKey;

    // 1) PEM 문자열 (public key) -> RSAPublicKey
    private RSAPublicKey parseRsaPublicKey(String pem) throws Exception {

        String nativeKey = pem
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(nativeKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey keys = keyFactory.generatePublic(keySpec);
        return (RSAPublicKey) keys;

    }

    // 2) JwtDecoder
    @Bean
    public JwtDecoder jwtDecoder() throws Exception {

        RSAPublicKey rsaPublicKey = parseRsaPublicKey(publicKey);
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();

        //기본 검증기 - 유효 기간 등
        OAuth2TokenValidator<Jwt> validator = JwtValidators.createDefault();
        //추가 검증기 - type 검증
        OAuth2TokenValidator<Jwt> typeValidator =
                jwt -> {
                    String type = jwt.getClaimAsString("type");
                    if (!type.equals("access")) {
                        return OAuth2TokenValidatorResult.failure(
                                new OAuth2Error("token is invalid", "유효한 Token이 아닙니다", null));
                    }
                    return OAuth2TokenValidatorResult.success();
                };

        jwtDecoder.setJwtValidator(
                new DelegatingOAuth2TokenValidator<>(validator, typeValidator)
        );

        return jwtDecoder;

    }
}

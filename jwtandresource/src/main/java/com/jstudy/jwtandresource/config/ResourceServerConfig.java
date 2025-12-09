package com.jstudy.jwtandresource.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
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
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
    }

}

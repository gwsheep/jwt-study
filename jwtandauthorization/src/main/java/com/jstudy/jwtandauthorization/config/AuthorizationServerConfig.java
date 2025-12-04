package com.jstudy.jwtandauthorization.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class AuthorizationServerConfig {

    @Value("${spring.jwt.keystore.location}")
    private Resource keystoreLocation;

    @Value("${spring.jwt.keystore.password}")
    private String keystorePassword;

    @Value("${spring.jwt.keystore.key-alias}")
    private String keyAlias;

    @Value("${spring.jwt.keystore.key-password}")
    private String keyPassword;

    //1) KeyStore(ex: .jks, .pem ..) or KeyPair
    private KeyStore getKeyStore() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(keystoreLocation.getInputStream(), keystorePassword.toCharArray());
        return keyStore;
    }

    //2) RSA Key
    private RSAKey getRSAKey() throws Exception {
        KeyStore keyStore = getKeyStore();
        Key key = keyStore.getKey(keyAlias, keyPassword.toCharArray());
        Certificate certificate = keyStore.getCertificate(keyAlias);
        PublicKey publicKey = certificate.getPublicKey();

        return new RSAKey.Builder((RSAPublicKey) publicKey)
                    .privateKey((RSAPrivateKey) key)
                    .keyID(keyAlias)
                    .build();
    }

    //3) JwkStore
    @Bean
    public JWKSource<SecurityContext> jwkSource() throws Exception {
        RSAKey rsaKey = getRSAKey();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return  ((jwkSelector, securityContext) -> jwkSelector.select(jwkSet));
    }

    //+ 4) JwtEncoder
    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

}

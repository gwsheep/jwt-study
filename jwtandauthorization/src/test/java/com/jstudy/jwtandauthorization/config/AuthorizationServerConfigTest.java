package com.jstudy.jwtandauthorization.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Base64;

@SpringBootTest
public class AuthorizationServerConfigTest {

    @Value("${spring.jwt.keystore.location}")
    private Resource keystoreLocation;

    @Value("${spring.jwt.keystore.password}")
    private String keystorePassword;

    @Value("${spring.jwt.keystore.key-alias}")
    private String keyAlias;

    @Value("${spring.jwt.keystore.key-password}")
    private String keyPassword;

    @Test
    void KeyStore_AND_KEY_INFO_확인() throws Exception {

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(keystoreLocation.getInputStream(), keystorePassword.toCharArray());
        Key key = keyStore.getKey(keyAlias, keyPassword.toCharArray());
        System.out.println("==== KeyStore ====");
        System.out.println(keyStore.getKey(keyAlias, keyPassword.toCharArray()));
        System.out.println(keyStore.getClass());
        System.out.println(keyStore.getProvider());
        System.out.println(keyStore.getType());
        System.out.println("==== Key ====");
        System.out.println(key.getEncoded());
        System.out.println(key.getFormat());
        System.out.println(key.getClass());
    }

    @Test
    void 공개키_추출() throws Exception {
        String encoded = Base64.getEncoder().encodeToString(getPublicKey().getEncoded());
        System.out.println("-----BEGIN PUBLIC KEY-----");
        System.out.println(encoded);
        System.out.println("-----END PUBLIC KEY-----");
    }

    private PublicKey getPublicKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(keystoreLocation.getInputStream(), keystorePassword.toCharArray());
        Certificate certificate = keyStore.getCertificate(keyAlias);
        return certificate.getPublicKey();
    }


}

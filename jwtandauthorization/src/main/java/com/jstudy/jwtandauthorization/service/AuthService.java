package com.jstudy.jwtandauthorization.service;

import com.jstudy.jwtandauthorization.dto.RequestVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${spring.security.oauth2.authorization-server.issuer}")
    private String issuer;

    private final JwtEncoder jwtEncoder;

    public Map<String, Object> createAccessToken(RequestVO req) {

        Instant issuedDate = Instant.now();
        Instant expiryDate = issuedDate.plus(1, ChronoUnit.DAYS);

        //Jwt Customize - 정적 생성
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer(issuer)                   //발급 주체
                .subject(req.getId())               //사용 주체
                .id(UUID.randomUUID().toString())   //JWT ID
                .issuedAt(issuedDate)               //발급 시각
                .expiresAt(expiryDate)              //유효 시각
                //.claim()                          //Customize
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();

        return Map.of(
                "access_token", token,
                "token_type", "Bearer",
                "expiry_date", expiryDate
        );

    }

}

package com.jstudy.jwtandauthorization.service;

import com.jstudy.jwtandauthorization.dto.JwtVO;
import com.jstudy.jwtandauthorization.dto.RequestVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${spring.security.oauth2.authorization-server.issuer}")
    private String issuer;

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public JwtVO createToken(RequestVO req) {

        Instant issuedDate = Instant.now();
        Instant expiryDate = issuedDate.plus(3, ChronoUnit.MINUTES);
        Instant expiryDateForRefresh = issuedDate.plus(30, ChronoUnit.MINUTES);
        String id = UUID.randomUUID().toString();

        //Jwt - Access Token    //Jwt Customize - 정적 생성
        JwtClaimsSet accessClaimsSet = JwtClaimsSet.builder()
                .issuer(issuer)                   //발급 주체
                .subject(req.getId())               //사용 주체
                .id(id)   //JWT ID
                .issuedAt(issuedDate)               //발급 시각
                .expiresAt(expiryDate)              //유효 시각
                .claim("type","access")                          //Customize
                .build();
        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(accessClaimsSet)).getTokenValue();

        //Jwt - Refresh Token
        JwtClaimsSet refreshClaimsSet = JwtClaimsSet.builder()
                .issuer(issuer)                   //발급 주체
                .subject(req.getId())               //사용 주체
                .id(id)   //JWT ID
                .issuedAt(issuedDate)
                .expiresAt(expiryDateForRefresh)
                .claim("type","refresh")
                .build();
        String refreshToken = jwtEncoder.encode(JwtEncoderParameters.from(refreshClaimsSet)).getTokenValue();

        return new JwtVO(accessToken, refreshToken, "Bearer", expiryDate.toString());

    }

    public JwtVO refreshToken(JwtVO req) {

        //refresh 검증
        Jwt refreshToken = jwtDecoder.decode(req.getRefresh_token());

        if(!refreshToken.getClaimAsString("type").equals("refresh")) {
            throw new IllegalStateException("Invalid refresh token : refresh 아님");
        } else if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalStateException("Invalid refresh token : 유효기간 지남");
        }

        //access 생성
        Instant issuedDate = Instant.now();
        Instant expiryDate = issuedDate.plus(3, ChronoUnit.MINUTES);
        JwtClaimsSet accessClaimsSet = JwtClaimsSet.builder()
                .issuer(issuer) //발급 주체
                .subject(refreshToken.getSubject())          //사용 주체
                .id(refreshToken.getId())           //JWT ID
                .issuedAt(issuedDate)               //발급 시각
                .expiresAt(expiryDate)              //유효 시각
                .claim("type","access")                          //Customize
                .build();
        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(accessClaimsSet)).getTokenValue();

        return new JwtVO(accessToken, refreshToken.getTokenValue(), "Bearer", expiryDate.toString());

    }

}

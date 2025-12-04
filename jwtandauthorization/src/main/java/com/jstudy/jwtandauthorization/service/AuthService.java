package com.jstudy.jwtandauthorization.service;

import com.jstudy.jwtandauthorization.dto.RequestVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtEncoder jwtEncoder;

    public void createAccessToken(RequestVO req) {

        //tmp configuration
//        long expiry = ;
//
//        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
//                .issuer("")
//                .issuedAt()
//                .expiresAt(expiry)
//                .subject()
//                .claim()
//                .build();
//
//        jwtEncoder.encode(JwtEncoderParameters.from());

    }

}

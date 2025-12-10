package com.jstudy.jwtandauthorization.controller;


import com.jstudy.jwtandauthorization.dto.JwtVO;
import com.jstudy.jwtandauthorization.dto.RequestVO;
import com.jstudy.jwtandauthorization.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/jwt")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/issue")
    public ResponseEntity<?> createJwt(@RequestBody RequestVO req) {
        JwtVO accessToken = authService.createToken(req);
        return ResponseEntity.ok(accessToken);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshJwt(@RequestBody JwtVO req) {
        /**
         * 중간에 token 탈취 방지 코드 넣어보기
         */
        JwtVO refreshedToken = authService.refreshToken(req);
        return ResponseEntity.ok(refreshedToken);
    }

}

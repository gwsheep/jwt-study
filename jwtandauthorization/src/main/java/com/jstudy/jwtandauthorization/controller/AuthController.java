package com.jstudy.jwtandauthorization.controller;


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
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    //JWT 발급 Controller
    @PostMapping("/issue/jwt")
    public ResponseEntity<?> issueJwt(@RequestBody RequestVO req) {
        Map<String, Object> accessToken = authService.createAccessToken(req);
        return ResponseEntity.ok(accessToken);
    }

}

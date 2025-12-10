package com.jstudy.jwtandresource.controller;

import com.jstudy.jwtandresource.service.ResourceService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @GetMapping("/resource")
    public ResponseEntity<?> resourceTest(@RequestParam("name") @NonNull String name) {
        /**
         * public key이 아닌 issuer-uri 활용한 jwt 검증 방법 연습해보기
         */
        String parsedName = resourceService.parseName(name);
        return ResponseEntity.ok(parsedName);
    }

}

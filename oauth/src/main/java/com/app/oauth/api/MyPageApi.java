package com.app.oauth.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/private")
@Slf4j
public class MyPageApi {

    @PostMapping("my-page-test")
    public void test(Authentication authentication) {
        log.info("토큰 private 테스트 : {}", authentication.getPrincipal().toString());
    }
}

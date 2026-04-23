package com.app.oauth.util;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@Slf4j
public class JwtTokenUtilTest {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    public void test(){
        Map<String,String> map = new HashMap<>();
        map.put("id","1");
        map.put("memberEmail","hong123@gmail.com");

        String accessToken = jwtTokenUtil.generateAccessToken(map);
        String refreshToken = jwtTokenUtil.generateRefreshToken(map);

        log.info("accessToken:{}",accessToken);
        log.info("refreshToken:{}",refreshToken);
    }

    @Test
    public void parseTokenTest(){
        Map<String,String> map = new HashMap<>();
        map.put("id","1");
        map.put("memberEmail","hong123@gmail.com");

        String accessToken = jwtTokenUtil.generateAccessToken(map);
        String refreshToken = jwtTokenUtil.generateRefreshToken(map);

        Claims claims = jwtTokenUtil.parseToken("이규학");
        log.info("claims:{}",claims);
    }

//    토큰 유효성 검사 테스트
    @Test
    public void validateTokenTest(){
        Map<String,String> map = new HashMap<>();
        map.put("id","1");
        map.put("memberEmail","hong123@gmail.com");
        String accessToken = jwtTokenUtil.generateAccessToken(map);

        log.info("validate: {}", jwtTokenUtil.validateToken("이규학"));
    }
}

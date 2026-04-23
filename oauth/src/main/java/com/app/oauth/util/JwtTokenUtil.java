package com.app.oauth.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String secretKey;

//    토큰 만료시간 각각 달라서 매서드 각각 따로 만들어야함
//    토큰 생성해서 만들어야 함 (엑세스 토큰)
    public String generateAccessToken(Map<String, String> claims) {
        String id = claims.get("id");
        String memberEmail = claims.get("memberEmail");

//        토큰 만료시간 정해야함
//        평균 1~5분 정도 (수업 테스트 에서는 24시간)
        long expirationTimeInMills = 1000 * 60 * 60 * 24;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTimeInMills);

        claims.put("issuer", "a_class");
        return Jwts.builder()
                .claims(claims) // 클레임
                .expiration(expirationDate) // 알고리즘
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setHeaderParam("typ", "JWT")
                .compact();
    }

//    리프레시 토큰 생성 매서드

//    토큰 만료시간 각각 달라서 매서드 각각 따로 만들어야함
//    토큰 생성해서 만들어야 함 (리프레시 토큰)
    public String generateRefreshToken(Map<String, String> claims) {
        String id = claims.get("id");
        String memberEmail = claims.get("memberEmail");

//        토큰 만료시간 정해야함
//        평균 1주일 ~ 30일 정도
        long expirationTimeInMills = 1000 * 60 * 60 * 24 * 30;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTimeInMills);

        claims.put("issuer", "a_class");
        return Jwts.builder()
                .claims(claims) // 클레임
                .expiration(expirationDate) // 알고리즘
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setHeaderParam("typ", "JWT")
                .compact();
    }

    // 토큰 파싱(token -> claim)
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException();
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException();
        } catch (MalformedJwtException e) {
            throw new RuntimeException();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException();
        }
    }

    // 토큰 유효성 검사
    public Map<String, Object> validateToken(String token) {
        Map<String, Object> result = new HashMap<>();
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            result.put("success", true);
            result.put("message", "토큰 파싱 완료");
            return result;

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return result;
        }
    }
}

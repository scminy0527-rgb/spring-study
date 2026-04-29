package com.app.oauth.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class AuthCodeGenerator {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

//    000000 부터 999999 까지 랜덤한 숫자
    public static String generateByRange() {
        int code = SECURE_RANDOM.nextInt(1_000_000); // 0 ~ 999999
        return String.format("%06d", code);           // 000000 ~ 999999
    }
}

package com.app.oauth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class AuthServiceTest {
//    휴대폰 인증 순서
//    1. 랜덤 번호 생성
//    2. 해당 번호를 인증 번호로써 사용자한테 문자 보냄과 동시에 ttl 설정 해서 redis 에 저장
//    3. 사용자가 값을 입력하면 인증 번호가 일치 하는지 확인
//    4. 인증 성공
}

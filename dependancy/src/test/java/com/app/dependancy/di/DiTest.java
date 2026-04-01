package com.app.dependancy.di;

import lombok.Data;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class DiTest {

//    Test 에서는 생성자 주입을 받을 수 없다
//    필드 주입
    @Autowired
    private Coding coding;

    @Autowired
    private Food food;

    @Autowired
    private Login login;

    @Test
    public void codingTest(){
        log.info("codingTest");
        log.info("coding : {}",coding.getComputer());
    }

    @Test
    public void foodTest(){
        log.info("음식 테스트 foodTest");
        log.info("food {}", food.getKnife());
    }

    @Test
    public void loginTest(){
        log.info("멤버 테스트");
        log.info("멤버 객체 주소값: {} ", login.getMember());
    }
}

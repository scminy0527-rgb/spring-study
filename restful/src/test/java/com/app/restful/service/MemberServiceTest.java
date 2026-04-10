package com.app.restful.service;

import com.app.restful.domain.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class MemberServiceTest {
    @Autowired
    MemberServiceImpl memberService;

    @Test
    public void getMemberTest(){
        log.info("getMemberTest: {}", memberService.getMemberInfo(1L));
    }

//    멤버 전체 가져오는 테스트
    @Test
    public void getMembersTest(){
        log.info("멤버 전체 조회:");
        memberService.getMembers().stream()
                .forEach(member -> log.info(member.toString()));
    }
}

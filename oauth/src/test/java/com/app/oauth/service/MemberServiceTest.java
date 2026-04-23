package com.app.oauth.service;

import com.app.oauth.domain.dto.JwtTokenDTO;
import com.app.oauth.domain.dto.MemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Test
    public void testMemberService() {
        log.info("hello world");
    }

    @Test
    public void joinTest(){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberEmail("test123@gmail.com");
        memberDTO.setMemberPassword("test123!@#");
        memberDTO.setMemberName("홍길동");
        memberDTO.setMemberNickname("개복치 홍길동");

        memberService.join(memberDTO);
    }

    @Test
    public void loginTest(){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberEmail("test123@gmail.com");
        memberDTO.setMemberPassword("test123!@#");

        JwtTokenDTO tokenDTO = memberService.login(memberDTO);
        log.info("token:{}",tokenDTO);
    }
}

package com.app.threetier.service;

import com.app.threetier.domain.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class MemberServiceTests {
    @Autowired
    private MemberServiceImpl memberService;

    @Test
    public void joinMemberTest(){
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberEmail("test111@gmail.com");
        memberVO.setMemberName("이보고");
        memberVO.setMemberPassword("test123");
        memberService.joinMember(memberVO);
    }

    @Test
    public void loginMemberTest(){
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberEmail("test321@gmail.com");
        memberVO.setMemberPassword("test123");
        log.info("로그인 멤버: {}", memberService.loginMember(memberVO));
    }
}

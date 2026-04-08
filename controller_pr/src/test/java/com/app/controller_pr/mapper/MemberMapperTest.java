package com.app.controller_pr.mapper;

import com.app.controller_pr.domain.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@Slf4j
public class MemberMapperTest {
    @Autowired
    MemberMapper memberMapper;

    @Test
    void joinTest() {
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberName("memberName");
        memberVO.setMemberPassword("memberPassword");
        memberVO.setMemberEmail("memberEmail@gmail.com");
        memberMapper.join(memberVO);
    }

    @Test
    void loginTest() {
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberEmail("memberEmail@gmail.com");
        memberVO.setMemberPassword("memberPassword");
        Optional<MemberVO> foundMember = memberMapper.selectOneByEmeberEmailAndMemberPassword(memberVO);

        if (!foundMember.isPresent()) {
            log.info("로그인 정보 없음");
            return;
        }

        MemberVO member = foundMember.get();
        log.info("멤버: {}", member.toString());
    }
}

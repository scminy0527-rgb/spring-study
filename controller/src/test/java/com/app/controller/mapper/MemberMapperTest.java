package com.app.controller.mapper;

import com.app.controller.domain.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@Slf4j
@SpringBootTest
public class MemberMapperTest {

    @Autowired
    private MemberMapper memberMapper;

    @Test
    public void memberInsertTest() {
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberEmail("test345@gmail.com");
        memberVO.setMemberName("kim yeonghei");
        memberVO.setMemberPassword("test123!@#");
        memberMapper.insert(memberVO);
    }

//    로그인 테스트
    @Test
    public void memberLoginTest() {
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberEmail("test123@gmail.com");
        memberVO.setMemberPassword("test3");

        if(memberMapper.existByMemberEmail(memberVO.getMemberEmail()) == 0){
            log.info("존재하지 않는 이메일 주소 입니다.");
            return;
        }

        Optional<MemberVO> foundMember = memberMapper.selectByMemberEmailAndMemberPassword(memberVO);
        if(!foundMember.isPresent()){
            log.info("비밀번호가 올바르지 않습니다.");
            return;
        }

        MemberVO member = foundMember.get();
        log.info("로그인 한 정보: {}", member);
    }
}


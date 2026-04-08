package com.app.ncs.mapper;

import com.app.ncs.domain.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@Slf4j
public class MemberMapperTest {

    @Autowired
    private MemberMapper memberMapper;

    @Test
    public void joinTest() {
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberName("최길동");
        memberVO.setMemberEmail("test0000@gmail.com");
        memberVO.setMemberPassword("test123!@#");
        memberMapper.join(memberVO);
    }

    @Test
    public void loginTest() {
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberEmail("test1234@gmail.com");
        memberVO.setMemberPassword("tes123!@#");
        Optional<MemberVO> foundMember = memberMapper.selectOneByMemberEmailAndMemberPassword(memberVO);

        if(!foundMember.isPresent()) {
            log.info("로그인 실패");
            return;
        }

        MemberVO member =  foundMember.get();
        log.info("로그인 성공: {}", member.toString());
    }

    @Test
    public void updateTest() {
        MemberVO memberVO = new MemberVO();
        memberVO.setId(88L);
        memberVO.setMemberName("박규혁");
        memberVO.setMemberEmail("test1234@gmail.com");
        memberVO.setMemberPassword("test");
        memberMapper.update(memberVO);
    }
}

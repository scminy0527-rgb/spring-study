package com.app.restful.service;

import com.app.restful.domain.dto.MemberJoinRequestDTO;
import com.app.restful.domain.dto.MemberLoginRequestDTO;
import com.app.restful.domain.dto.MemberUpdateRequestDTO;
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
    @Autowired
    private MemberJoinRequestDTO memberJoinRequestDTO;

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

    @Test
    public void getMemberByIdTest(){
        MemberJoinRequestDTO memberJoinRequestDTO = new MemberJoinRequestDTO();
        memberJoinRequestDTO.setMemberEmail("test1173316@gmail.com");
        memberJoinRequestDTO.setMemberName("james");
        memberJoinRequestDTO.setMemberPassword("1234");
        memberService.join(memberJoinRequestDTO);
    }

//    이메일 중복 여부 체크 테스트
//    @Test
//    public void checkEmailDuplicateTest(){
//        log.info("해당 이메일 존재 갯수: {}",
//                memberService.checkEmailDuplicate("cjfals1015@gmail.com").toS);
//    }

//    로그인 테스트
//    @Test
//    public void loginTest(){
//        MemberLoginRequestDTO memberLoginRequestDTO = new MemberLoginRequestDTO();
//        memberLoginRequestDTO.setMemberEmail("test123@gmail.com");
//        memberLoginRequestDTO.setMemberPassword("test123");
//
//        log.info("loginTest: {}", memberService.login(memberLoginRequestDTO).get());
//    }

//    멤버 정보 수정 테스트
    @Test
    public void modifyMemberInfoTest(){
        MemberUpdateRequestDTO memberUpdateRequestDTO = new MemberUpdateRequestDTO();
//        memberUpdateRequestDTO.setMemberName("이서령");
        memberUpdateRequestDTO.setMemberPassword("test123!@#");
        memberService.modifyMemberInfo(memberUpdateRequestDTO, 121L);
    }

    @Test
    public void withdrawMemberTest(){
        memberService.withdrawMember(128L);
    }
}

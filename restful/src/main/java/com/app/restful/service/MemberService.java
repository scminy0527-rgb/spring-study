package com.app.restful.service;

import com.app.restful.domain.dto.MemberJoinRequestDTO;
import com.app.restful.domain.dto.MemberLoginRequestDTO;
import com.app.restful.domain.dto.MemberResponseDTO;
import com.app.restful.domain.dto.MemberUpdateRequestDTO;
import com.app.restful.domain.vo.MemberVO;

import java.util.List;
import java.util.Optional;

public interface MemberService {
//    회원 가입
    public void join(MemberJoinRequestDTO memberJoinRequestDTO);

//    이메일 중복 여부 확인
    public void checkEmailDuplicate(String email);

//    아이디 존재 여부 확인
    public void checkMemberIdExists(Long id);

//    로그인 (사실은 token 을 반환하는걸로 해야 함)
    public MemberResponseDTO login(MemberLoginRequestDTO memberLoginRequestDTO);

    public void checkUserExist(MemberVO memberVO);

//    회원 정보 조회
    public MemberResponseDTO getMemberInfo(Long id);

//    멤버 전체 정보 조회
    public List<MemberResponseDTO> getMembers();

//    회원 수정
    public void modifyMemberInfo(MemberUpdateRequestDTO memberUpdateRequestDTO);

//    회원 비밀번호 변경(마이페이지)
//    회원 비밀번호 변경(로그인 전)

//    회원 탈퇴
    public void withdrawMember (Long id);
}

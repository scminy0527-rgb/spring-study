package com.app.restful.service;

import com.app.restful.domain.dto.MemberResponseDTO;
import com.app.restful.domain.vo.MemberVO;

import java.util.List;
import java.util.Optional;

public interface MemberService {
//    회원 가입
    public void join(MemberVO memberVO);

//    로그인 (사실은 token 을 반환하는걸로 해야 함)
    public Optional<MemberVO> login(MemberVO memberVO);

//    회원 정보 조회
    public Optional<MemberResponseDTO> getMemberInfo(Long id);

//    멤버 전체 정보 조회
    public List<MemberResponseDTO> getMembers();

//    회원 수정
//    회원 비밀번호 변경(마이페이지)
//    회원 비밀번호 변경(로그인 전)

//    회원 탈퇴
}

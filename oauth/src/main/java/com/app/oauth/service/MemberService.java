package com.app.oauth.service;

import com.app.oauth.domain.dto.JwtTokenDTO;
import com.app.oauth.domain.dto.MemberDTO;

import java.util.HashMap;
import java.util.Map;

public interface MemberService {
//    회원 가입
    public Map<String, Object> join(MemberDTO memberDTO);

//    단지 멤버 중복 여부를 확인하는 함수
//    public boolean checkEmailDu

//    로그인 (토큰 기반)
//    로컬 로그인
    public JwtTokenDTO login(MemberDTO memberDTO);

//    소셜 로그인
    public void socialLogin(MemberDTO memberDTO);

//    마이 페이지 회원 수정
//    회원 탈퇴
}

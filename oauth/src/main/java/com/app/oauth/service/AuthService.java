package com.app.oauth.service;


import com.app.oauth.domain.dto.JwtTokenDTO;
import com.app.oauth.domain.dto.MemberDTO;

public interface AuthService {
    //    로그인 (토큰 기반)
//    로컬 로그인
    public JwtTokenDTO login(MemberDTO memberDTO);

    //    소셜 로그인
    public void socialLogin(MemberDTO memberDTO);

//    레디스에 보관

//    블랙리스트 (로그아웃 시 토큰 무효화)

//    Redis 에 블랙리스트 인지 검증

//    리프레시 토큰 검증 하고 새 토큰을 발급하기 위한 서비스
    public JwtTokenDTO reIssueAccessToken(JwtTokenDTO jwtTokenDTO);

//    레디스에 저장된 refresh 토큰이 유효한지 검증하는 코드
}

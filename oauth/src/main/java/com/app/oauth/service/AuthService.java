package com.app.oauth.service;

import com.app.oauth.domain.dto.JwtTokenDTO;
import com.app.oauth.domain.dto.MemberDTO;
import com.app.oauth.domain.dto.request.VerificationRequestDTO;

public interface AuthService {
    // 로컬 로그인
    public JwtTokenDTO login(MemberDTO memberDTO);

    // 소셜 로그인
    public JwtTokenDTO socialLogin(MemberDTO memberDTO);

//    로그아웃
//    로그아웃은 원래 void 로 처리 하는게 관례
    public void logout(JwtTokenDTO jwtTokenDTO);

    // Redis에 refresh Token 저장
    public boolean saveRefreshToken(JwtTokenDTO jwtTokenDTO);

    // Redis에 저장된 refresh Token이 유효한지 검증
    public boolean validateRefreshToken(JwtTokenDTO jwtTokenDTO);

    // Redis에 블랙리스트를 등록 (로그아웃 시 토큰 무효화)
    public boolean saveBlackListedToken(JwtTokenDTO jwtTokenDTO);

    // Redis에 등록된 블랙리스트인지 검증
    public boolean isBlackListedToken(JwtTokenDTO jwtTokenDTO);

    // refresh 토큰을 검증하고, 새로운 accessToken 발급 서비스
    public JwtTokenDTO reissueAccessToken(JwtTokenDTO jwtTokenDTO);

//    인증 진행
    public boolean sendMemberPhoneVerificationCode(VerificationRequestDTO verificationRequestDTO);


    public boolean verifyMemberPhoneVerificationCode(String phoneNumber);


//    인증 번호 생성



//    인증 번호 전송

//    인증 번호 redis 에 보관

//    인증 번호 매칭 여부 검증

//    이메일 인증 번호


}

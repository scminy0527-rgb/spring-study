package com.app.practice.service;

import com.app.practice.domain.dto.UserJoinRequestDTO;
import com.app.practice.domain.dto.UserLoginRequestDTO;
import com.app.practice.domain.dto.UserResponseDTO;
import com.app.practice.domain.dto.UserUpdateRequestDTO;
import com.app.practice.domain.vo.UserVO;

public interface UserService {
//    유저 회원가입
    public void registerUser(UserJoinRequestDTO userJoinRequestDTO);

//    유저 이메일 중복 여부 확인
    public void checkEmailDuplicate(String userEmail);

//    아이디로 유저 찾기
    public UserResponseDTO getUserById(Long id);

//    유저 로그인 서비스
    public UserResponseDTO loginUser(UserLoginRequestDTO userLoginRequestDTO);

//    유저 정보 수정 서비스
    public void modifyUserInfo(Long id, UserUpdateRequestDTO userUpdateRequestDTO);
}

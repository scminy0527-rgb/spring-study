package com.app.rest_pr.service;

import com.app.rest_pr.domain.dto.UserJoinRequestDTO;
import com.app.rest_pr.domain.dto.UserLoginRequestDTO;
import com.app.rest_pr.domain.dto.UserResponseDTO;
import com.app.rest_pr.domain.dto.UserUpdateRequestDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
//    유저 회원가입 서비스
    public void registerUser(UserJoinRequestDTO userJoinRequestDTO);

//    유저 중복여부 조회
    public void checkEmailDuplicate(String email);

//    유저 전체 조회 서비스
    public List<UserResponseDTO> getAllUsers(String order);

//    유저 로그인 서비스
    public UserResponseDTO loginUser(UserLoginRequestDTO  userLoginRequestDTO);

//    단일 유저 조회 서비스
    public UserResponseDTO getUserInfo(Long id);

//    유저 정보 수정 서비스
    public void updateUserInfo(Long id, UserUpdateRequestDTO userUpdateRequestDTO);

//    유저 탈퇴 서비스
    public void withdrawUser(Long id);
}

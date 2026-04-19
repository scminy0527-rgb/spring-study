package com.app.rest_pr.service;

import com.app.rest_pr.domain.dto.UserJoinRequestDTO;
import com.app.rest_pr.domain.dto.UserLoginRequestDTO;
import com.app.rest_pr.domain.dto.UserResponseDTO;
import com.app.rest_pr.domain.dto.UserUpdateRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class UserServiceTest {
    @Autowired
    private UserService userService;

//    회원가입 테스트 서비스
    @Test
    public void testRegisterUser() {
        UserJoinRequestDTO userJoinRequestDTO = new UserJoinRequestDTO();
        userJoinRequestDTO.setUserEmail("test345@gmail.com");
        userJoinRequestDTO.setUserName("장보고");
        userJoinRequestDTO.setUserPassword("test123!@#");
        userJoinRequestDTO.setUserNickname("행복이2");

        userService.registerUser(userJoinRequestDTO);
    }

//    유저 전체 정보 불러오는 서비스
    @Test
    public void testGetAllUser() {
        userService.getAllUsers().forEach((user) -> {
            log.info("user 의 정보 {}", user);
        });
    }

//    유저 입력 정보를 토대로 해서 로그인을 하는 함수
    @Test
    public void testLoginUser() {
        UserLoginRequestDTO  userLoginRequestDTO = new UserLoginRequestDTO();
        userLoginRequestDTO.setUserEmail("test123@gmail.com");
        userLoginRequestDTO.setUserPassword("test123!@#");
        UserResponseDTO user = userService.loginUser(userLoginRequestDTO);
        log.info("로그인 한 유저: {}", user);
    }

//    아이디로 유저 불러오는 거 테스트
    @Test
    public void testGetUserInfo() {
        UserResponseDTO user = userService.getUserInfo(999L);
        log.info("유저 불러오기 정보: {}", user);
    }

//    유저 정보 수정하는 테스트
    @Test
    public void testUpdateUserInfo() {
        UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO();
        userUpdateRequestDTO.setUserName("박영재");
        userUpdateRequestDTO.setUserPassword("test123");
        userUpdateRequestDTO.setUserNickname("러거");
        userService.updateUserInfo(7L, userUpdateRequestDTO);
    }

//    유저 탈퇴하는 서비스
    @Test
    public void testWithdrawUser() {
        userService.withdrawUser(7L);
    }
}

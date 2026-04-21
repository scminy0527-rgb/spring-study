package com.app.practice.service;

import com.app.practice.domain.dto.UserJoinRequestDTO;
import com.app.practice.domain.dto.UserLoginRequestDTO;
import com.app.practice.domain.dto.UserResponseDTO;
import com.app.practice.domain.dto.UserUpdateRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class UserServiceTest {
    @Autowired
    private UserService userService;

//    회원가입 테스트
    @Test
    public void testRegisterUser() {
        UserJoinRequestDTO userJoinRequestDTO = new UserJoinRequestDTO();
        userJoinRequestDTO.setUserName("test");
        userJoinRequestDTO.setUserEmail("youngleeprint@gmail.com");
        userJoinRequestDTO.setUserPassword("test123!@#");
        userJoinRequestDTO.setUserNickname("test");

        userService.registerUser(userJoinRequestDTO);
    }

//    유저 이메일 중복 테스트
    @Test
    public void testCheckEmailDuplicate() {
        userService.checkEmailDuplicate("igh@gamil.com");
        log.info("이메일 중복 발생 안함");
    }

//    아이디로 유저 찾기 테스트
    @Test
    public void testGetUserById() {
        UserResponseDTO  userResponseDTO = userService.getUserById(24L);
        log.info("유저 결과: {}", userResponseDTO);
    }

//    유저 로그인 테스트
    @Test
    public void testUserLogin(){
        UserLoginRequestDTO userLoginRequestDTO = new UserLoginRequestDTO();
        userLoginRequestDTO.setUserEmail("test123@gmail.com");
        userLoginRequestDTO.setUserPassword("test123");
        UserResponseDTO user =  userService.loginUser(userLoginRequestDTO);

        log.info("로그인 유저:{}", user);
    }

//    유저 정보 수정 테스트
    @Test
    public void testModifyUserInfo(){
        UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO();
        userUpdateRequestDTO.setUserName("test111");
        userUpdateRequestDTO.setUserPassword("test123456");
        userUpdateRequestDTO.setUserNickname("test111");
        userService.modifyUserInfo(50L, userUpdateRequestDTO);
    }
}

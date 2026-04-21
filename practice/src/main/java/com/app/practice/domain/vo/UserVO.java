package com.app.practice.domain.vo;

import com.app.practice.domain.dto.UserJoinRequestDTO;
import com.app.practice.domain.dto.UserLoginRequestDTO;
import com.app.practice.domain.dto.UserUpdateRequestDTO;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserVO {
    private Long id;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userNickname;

//    정적 팩토리 매서드
    public static UserVO from(UserJoinRequestDTO userJoinRequestDTO) {
        UserVO userVO = new UserVO();
        userVO.setUserName(userJoinRequestDTO.getUserName());
        userVO.setUserEmail(userJoinRequestDTO.getUserEmail());
        userVO.setUserPassword(userJoinRequestDTO.getUserPassword());
        userVO.setUserNickname(userJoinRequestDTO.getUserNickname());

        return userVO;
    }

    public static UserVO from(UserLoginRequestDTO userLoginRequestDTO) {
        UserVO userVO = new UserVO();
        userVO.setUserEmail(userLoginRequestDTO.getUserEmail());
        userVO.setUserPassword(userLoginRequestDTO.getUserPassword());

        return userVO;
    }

    public static UserVO from(UserUpdateRequestDTO userUpdateRequestDTO) {
        UserVO userVO = new UserVO();
        userVO.setUserName(userUpdateRequestDTO.getUserName());
        userVO.setUserPassword(userUpdateRequestDTO.getUserPassword());
        userVO.setUserNickname(userUpdateRequestDTO.getUserNickname());

        return userVO;
    }
}

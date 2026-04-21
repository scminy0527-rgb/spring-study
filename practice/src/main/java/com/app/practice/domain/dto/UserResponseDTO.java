package com.app.practice.domain.dto;

import com.app.practice.domain.vo.UserVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@Schema(description = "유저 DTO 응답값")
public class UserResponseDTO {
    @Schema(description = "유저 아이디", example = "1", required = true)
    private Long id;
    @Schema(description = "유저 이름", example = "홍길동", required = true)
    private String userName;
    @Schema(description = "유저 이메일", example = "test123@gmail.com", required = true)
    private String userEmail;
    @Schema(description = "유저 닉네임", example = "규학이")
    private String userNickname;

//    정적 팩토리
    public static UserResponseDTO from(UserVO userVO) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(userVO.getId());
        userResponseDTO.setUserName(userVO.getUserName());
        userResponseDTO.setUserEmail(userVO.getUserEmail());
        userResponseDTO.setUserNickname(userVO.getUserNickname());
        return userResponseDTO;
    }
}

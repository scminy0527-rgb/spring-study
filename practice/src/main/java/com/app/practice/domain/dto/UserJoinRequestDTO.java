package com.app.practice.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@Schema(description = "유저 회원가입 DTO 필요 항목")
public class UserJoinRequestDTO {
    @Schema(description = "유저 이름", example = "홍길동", required = true)
    private String userName;
    @Schema(description = "유저 이메일", example = "test123@gmail.com", required = true)
    private String userEmail;
    @Schema(description = "유저 비밀번호", example = "test123!@#", required = true)
    private String userPassword;
    @Schema(description = "유저 닉네임", example = "규학이")
    private String userNickname;
}

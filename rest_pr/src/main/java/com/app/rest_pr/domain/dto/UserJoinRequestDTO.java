package com.app.rest_pr.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "회원 가입 요청 스키마", description = "회원가입을 할 때 필요한 요소 정의")
public class UserJoinRequestDTO {
    @Schema(description = "userName", example = "홍길동", required = true)
    private String userName;
    @Schema(description = "유저 이메일", example = "test123@gmail.com", required = true)
    private String userEmail;
    @Schema(description = "유저 비밀번호", example = "test123!@#", required = true)
    private String userPassword;
    @Schema(description = "유저 닉네임", example = "닉네임001")
    private String userNickname;
}

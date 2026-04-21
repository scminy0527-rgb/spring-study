package com.app.practice.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@Schema(description = "유저 정보 수정 DTO 필요 항목")
public class UserUpdateRequestDTO {
    @Schema(description = "유저 이름", example = "홍길동", required = true)
    private String userName;
    @Schema(description = "유저 비밀번호", example = "test123!@#", required = true)
    private String userPassword;
    @Schema(description = "유저 닉네임", example = "규학이")
    private String userNickname;
}

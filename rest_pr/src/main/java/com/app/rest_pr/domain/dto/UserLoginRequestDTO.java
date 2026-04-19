package com.app.rest_pr.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@Schema(description = "로그인을 할 때 필요한 req body")
public class UserLoginRequestDTO {
    @Schema(description = "유저 이메일", example = "test123@gmail.com", required = true)
    private String userEmail;
    @Schema(description = "유저 비밀번호", example = "test123!@#", required = true)
    private String userPassword;
}

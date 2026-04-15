package com.app.restful.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@Schema(description = "회원 가입 시 필요한 입력 값")
public class MemberJoinRequestDTO {
    @Schema(description = "회원 이메일", required = true, example = "test123@gmail.com")
    private String memberEmail;
    @Schema(description = "회원 비밀번호", required = true, example = "test123!@#")
    private String memberPassword;
    @Schema(description = "회원 이름", example = "홍길동")
    private String memberName;
}

package com.app.restful.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@Schema(description = "회원 정보 수정 DTO")
public class MemberUpdateRequestDTO {
    @Schema(description = "회원 번호", example = "1", required = true)
    private Long id;
    @Schema(description = "회원 비밀번호", required = true, example = "test123!@#")
    private String memberPassword;
    @Schema(description = "회원 이름", example = "홍길동", required = true)
    private String memberName;
}

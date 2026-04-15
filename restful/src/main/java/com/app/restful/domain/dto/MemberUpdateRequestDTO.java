package com.app.restful.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@Schema(description = "회원 정보 업데이트 시 필요 사항")
public class MemberUpdateRequestDTO {
    @Schema(description = "작성자 번호(해당 내용은 패스 배리어블로 해서 받을 예정)", example = "1")
    private Long id;
    @Schema(description = "회원 비밀번호", required = true, example = "test123!@#")
    private String memberPassword;
    @Schema(description = "회원 이름", example = "홍길동")
    private String memberName;
}

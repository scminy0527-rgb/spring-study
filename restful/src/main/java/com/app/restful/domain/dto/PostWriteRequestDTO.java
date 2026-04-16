package com.app.restful.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@Schema(description = "게시판 생성 DTO")
public class PostWriteRequestDTO {
    @Schema(description = "게시글 제목", required = true, example = "제목1.")
    private String postTitle;
    @Schema(description = "게시글 글 내용", required = true, example = "내용1")
    private String postContent;
//    (아이디는 나중에 토큰 에서 추출, 화면에서는 안받음)
//    private Long memberId;
}

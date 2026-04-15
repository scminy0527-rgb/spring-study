package com.app.restful.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@Schema(description = "게시글 내용 수정 시 필요 내용")
public class PostUpdateRequestDTO {
    @Schema(description = "게시글 번호 (패스 배리어블로 받아야 함)", example = "1")
    private Long id;
    @Schema(description = "게시글 제목", required = true, example = "제목000")
    private String postTitle;
    @Schema(description = "게시글 글 내용", required = true, example = "안녕하세요~~....")
    private String postContent;
    @Schema(description = "작성자 번호", required = true, example = "1")
    private Long memberId;
}

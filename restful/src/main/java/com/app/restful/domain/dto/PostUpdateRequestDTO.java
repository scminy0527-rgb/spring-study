package com.app.restful.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@Schema(description = "게시글 수정 DTO")
public class PostUpdateRequestDTO {
    @Schema(description = "수정된 게시글 제목", required = true, example = "제목000")
    private String postTitle;
    @Schema(description = "수정된 게시글 글 내용", required = true, example = "안녕하세요~~....")
    private String postContent;
}

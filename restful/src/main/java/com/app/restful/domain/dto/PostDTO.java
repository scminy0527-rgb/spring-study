package com.app.restful.domain.dto;

//SELECT TBP.ID, TBP.POST_TITLE, TBP.POST_CONTENT, TBP.MEMBER_ID, TBP.POST_READ_COUNT,
//TBM.MEMBER_NAME, TBM.MEMBER_EMAIL

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
@Schema(description = "게시글 DTO")
public class PostDTO implements Serializable {
    @Schema(description = "게시글 번호", required = true, example = "1")
    private Long id;
    @Schema(description = "게시글 제목", required = true, example = "제목000")
    private String postTitle;
    @Schema(description = "게시글 글 내용", required = true, example = "안녕하세요~~....")
    private String postContent;
    @Schema(description = "회원 번호", required = true, example = "1")
    private Long memberId;
    @Schema(description = "게시글 조회 수", example = "23")
    private Long postReadCount;

    @Schema(description = "회원 이름", example = "홍길동")
    private String  memberName;
    @Schema(description = "게시글 작성자 이메일", required = true, example = "test123@gmail.com")
    private String  memberEmail;
}

package com.app.mybatis.domain.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class PostDTO {
    private Long id;
    private String postTitle;
    private String postContent;
    private Long postReadCount;
    private String memberName;
    private String memberEmail;
    private Long memberId;
    private Long postLikeCount;
    private Long isClick;
    private boolean isLiked;
}

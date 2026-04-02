package com.app.mybatis.domain.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class PostVO {
    private Long id;
    private String postTitle;
    private String postContent;
    private Long postRaadCount;
    private Long memberId;
}

package com.app.restful.domain.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

//SELECT ID, POST_TITLE, POST_CONTENT, MEMBER_ID, POST_READ_COUNT
//FROM TBL_POST;

@Component
@Data
public class PostVO implements Serializable {
    private Long id;
    private String postTitle;
    private String postContent;
    private Long memberId;
    private Long postReadCount;
}

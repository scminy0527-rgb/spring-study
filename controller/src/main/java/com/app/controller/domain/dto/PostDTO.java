package com.app.controller.domain.dto;

//SELECT TBP.ID, TBP.POST_TITLE, TBP.POST_CONTENT, TBP.MEMBER_ID, TBP.POST_READ_COUNT,
//TBM.MEMBER_NAME, TBM.MEMBER_EMAIL

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
public class PostDTO implements Serializable {
    private Long id;
    private String postTitle;
    private String postContent;
    private Long memberId;
    private Long postReadCount;
    private String  memberName;
    private String  memberEmail;
}

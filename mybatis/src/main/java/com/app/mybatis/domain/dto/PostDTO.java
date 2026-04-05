package com.app.mybatis.domain.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class PostDTO {
//    해당 부분은 간단한 조인으로 가능
    private Long id;
    private String postTitle;
    private String postContent;
    private Long postReadCount;
    private String memberName;
    private String memberEmail;
    private Long memberId;

//    여기서 부터는 서브쿼리
    private Long postLikeCount;
    private boolean isClick;
}

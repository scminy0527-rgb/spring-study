package com.app.restful.domain.vo;

import com.app.restful.domain.dto.PostUpdateRequestDTO;
import com.app.restful.domain.dto.PostWriteRequestDTO;
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

//    PostWriteRequestDTO 를 PostVO 로 변환하는 팩토리 매서드
    public static PostVO from(PostWriteRequestDTO postWriteRequestDTO) {
        PostVO postVO = new PostVO();
        postVO.setPostTitle(postWriteRequestDTO.getPostTitle());
        postVO.setPostContent(postWriteRequestDTO.getPostContent());
        postVO.setMemberId(postWriteRequestDTO.getMemberId());

        return postVO;
    }

    public static PostVO from(PostUpdateRequestDTO postUpdateRequestDTO) {
        PostVO postVO = new PostVO();
        postVO.setId(postUpdateRequestDTO.getId());
        postVO.setPostTitle(postUpdateRequestDTO.getPostTitle());
        postVO.setPostContent(postUpdateRequestDTO.getPostContent());
        postVO.setMemberId(postUpdateRequestDTO.getMemberId());

        return postVO;
    }
}

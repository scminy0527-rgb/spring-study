package com.app.threetier.service;

import com.app.threetier.domain.vo.PostVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class PostServiceTests {

    @Autowired
    private PostService postService;

    @Test
    void getPostTest() {
        log.info("getPost: {}", postService.getPost(1000L));
    }

    @Test
    void updatePostTest() {
        PostVO postVO = new PostVO();
        postVO.setId(49L);
        postVO.setPostTitle("물은 영어로 무엇일까?");
        postVO.setPostContent("물은 SELF");

        postService.updatePost(postVO);
    }

    @Test
    void deletePostTest() {
        postService.deletePost(45L);
    }
}

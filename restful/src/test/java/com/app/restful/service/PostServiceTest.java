package com.app.restful.service;

import com.app.restful.domain.dto.PostDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class PostServiceTest {
    @Autowired
    private PostServiceImpl postService;

    @Test
    public void getAllPostsTest() {
        List<PostDTO> posts = postService.getAllPosts();
        log.info("포스트 전체 테스트");
        posts.stream()
                .forEach((post) -> {log.info(post.toString());});
    }

//    포스트 상세 불러오는 테스트
    @Test
    public void getPostTest() {
        PostDTO post = postService.getPost(999L);
        log.info("게시글: {}", post.toString());
    }
}

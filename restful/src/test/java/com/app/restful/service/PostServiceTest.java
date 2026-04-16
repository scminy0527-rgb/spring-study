package com.app.restful.service;

import com.app.restful.domain.dto.PostDTO;
import com.app.restful.domain.dto.PostUpdateRequestDTO;
import com.app.restful.domain.dto.PostWriteRequestDTO;
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

//    에러 나중에 해결
    @Test
    public void getAllPostsTest() {
        List<PostDTO> posts = postService.getAllPosts("desc");
        log.info("포스트 전체 테스트");
        posts.stream()
                .forEach((post) -> {log.info(post.toString());});
    }


    //    포스트 상세 불러오는 테스트
    @Test
    public void getPostDetailTest() {
        PostDTO post = postService.getPostDetail(999L);
        log.info("게시글: {}", post.toString());
    }

//    포스트 작성하는 테스트
    @Test
    public void writePostTest() {
        PostWriteRequestDTO postWriteRequestDTO = new PostWriteRequestDTO();
        postWriteRequestDTO.setPostContent("봄봄봄 봄이 왔어요");
        postWriteRequestDTO.setPostTitle("스프링2");

        postService.writePost(postWriteRequestDTO, 3L);
    }

//    포스트 내용 수정하는 테스트
    @Test
    public void updatePostTest() {
        PostUpdateRequestDTO postUpdateRequestDTO = new PostUpdateRequestDTO();
        postUpdateRequestDTO.setPostTitle("안녕");
        postService.updatePost(postUpdateRequestDTO, 102L);
    }

//    포스트 삭제하는 테스트
    @Test
    public void deletePostTest() {
        postService.deletePost(49L);
    }

//    회원 탈퇴 시 작성한 모든 게시글 삭제 서비스 테스트
    @Test
    public void deleteMembersAllPostsTest() {
        postService.deleteMembersAllPosts(3L);
    }
}

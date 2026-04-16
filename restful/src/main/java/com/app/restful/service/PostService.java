package com.app.restful.service;

import com.app.restful.domain.dto.PostDTO;
import com.app.restful.domain.dto.PostUpdateRequestDTO;
import com.app.restful.domain.dto.PostWriteRequestDTO;

import java.util.List;

public interface PostService {
//    모든 게시글 조회
    public List<PostDTO> getAllPosts(String orders);

//    게시글 상세조회
    public PostDTO getPostDetail(Long id);

//    게시글 작성
    public void writePost(PostWriteRequestDTO postWriteRequestDTO, Long memberId);

//    게시글 수정 (원래 대로 라면 여기서도 id 를 따로 해서 받는게 fm 방식임)
    public void updatePost(PostUpdateRequestDTO postUpdateRequestDTO, Long id);

//    게시글 삭제
    public void deletePost(Long id);

//    회원 탈퇴 시 작성한 모든 게시글 삭제
    public void deleteMembersAllPosts(Long memberId);
}

package com.app.restful.service;

import com.app.restful.domain.dto.PostDTO;
import com.app.restful.domain.vo.PostVO;

import java.util.List;

public interface PostService {
//    모든 게시글 조회
    public List<PostDTO> getAllPosts();

//    게시글 상세조회
    public PostDTO getPost(Long id);

//    게시글 작성
    public void writePost(PostVO  postVO);

//    게시글 수정
    public void updatePost(PostVO  postVO);

//    게시글 삭제
    public void deletePost(Long id);

//    회원 탈퇴 시 작성한 모든 게시글 삭제
    public void deleteMembersAllPosts(Long memberId);
}

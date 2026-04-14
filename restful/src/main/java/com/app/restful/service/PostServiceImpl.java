package com.app.restful.service;

import com.app.restful.domain.dto.PostDTO;
import com.app.restful.domain.vo.PostVO;
import com.app.restful.exceptions.PostNotFoundException;
import com.app.restful.repository.PostDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = {Exception.class})
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostDAO postDAO;

    //    모든 게시글 조회
    @Override
    public List<PostDTO> getAllPosts() {
        return postDAO.findAll();
    }

//    게시글 상세조회
    @Override
    public PostDTO getPost(Long id) {
        return postDAO.findById(id)
                .orElseThrow(() -> {
                    throw new PostNotFoundException("해당 포스트를 찾을 수 없습니다.");
                });
    }

//    게시글 작성
    @Override
    public void writePost(PostVO postVO) {

    }

//    게시글 수정
    @Override
    public void updatePost(PostVO postVO) {

    }

//    게시글 삭제
    @Override
    public void deletePost(Long id) {

    }

//    회원 탈퇴 시 작성한 모든 게시글 삭제
    @Override
    public void deleteMembersAllPosts(Long memberId) {

    }
}

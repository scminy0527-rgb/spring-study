package com.app.restful.service;

import com.app.restful.domain.dto.PostDTO;
import com.app.restful.domain.dto.PostUpdateRequestDTO;
import com.app.restful.domain.dto.PostWriteRequestDTO;
import com.app.restful.domain.vo.PostVO;
import com.app.restful.exceptions.PostException;
import com.app.restful.exceptions.PostNotFoundException;
import com.app.restful.repository.PostDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = {Exception.class})
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostDAO postDAO;
    private final MemberServiceImpl memberService;

    //    모든 게시글 조회
    @Override
    public List<PostDTO> getAllPosts(String order) {
        Map<String, String> orders = new HashMap<>();
        orders.put("order", order);
        return postDAO.findAll(orders);
    }

//    게시글 상세조회
    @Override
    public PostDTO getPostDetail(Long id) {
        return postDAO.findById(id)
                .orElseThrow(() -> {
                    throw new PostException("해당 포스트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
                });
    }

//    게시글 작성
    @Override
    public void writePost(PostWriteRequestDTO postWriteRequestDTO, Long memberId) {
        PostVO postVO = PostVO.from(postWriteRequestDTO);
        postVO.setMemberId(memberId);
        postDAO.save(postVO);
    }

//    게시글 수정
    @Override
    public void updatePost(PostUpdateRequestDTO  postUpdateRequestDTO, Long id) {
//     그런데 원래대로라면 이때 해당 멤버가 있는지 확인을 해야 함
//        memberService.checkMemberIdExists(postUpdateRequestDTO.getMemberId());
        PostVO postVO = PostVO.from(postUpdateRequestDTO);
        postVO.setId(id);
        postDAO.update(postVO);
    }

//    게시글 삭제
    @Override
    public void deletePost(Long id) {
        postDAO.delete(id);
    }

//    회원 탈퇴 시 작성한 모든 게시글 삭제
    @Override
    public void deleteMembersAllPosts(Long memberId) {
        postDAO.deleteByMemberId(memberId);
    }
}

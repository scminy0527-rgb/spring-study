package com.app.restful.repository;

import com.app.restful.domain.dto.PostDTO;
import com.app.restful.domain.vo.PostVO;
import com.app.restful.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostDAO {
    private final PostMapper postMapper;

//    모든 게시글 들 불러오기
    public List<PostDTO> findAll(Map<String, String> orders) {
        return postMapper.selectAll(orders);
    }

//    게시글 상세 불러오기
    public Optional<PostDTO> findById(long id) {
        return Optional.ofNullable(postMapper.select(id));
    }

//    게시글 작성하기 (작성은 주로 save 로 이름 함)
    public void save(PostVO postVO) {
        postMapper.insert(postVO);
    }

//    게시글 수정하기
    public void update(PostVO postVO) {
        postMapper.update(postVO);
    }

//    게시글 삭제하기
    public void delete(Long id) {
        postMapper.delete(id);
    }

//    회원 탈퇴 시 회원이 작성 한 게시글 일괄 삭제
    public void deleteByMemberId(Long memberId) {
        postMapper.deleteByMemberId(memberId);
    }
}

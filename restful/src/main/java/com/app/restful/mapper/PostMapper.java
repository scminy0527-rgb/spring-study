package com.app.restful.mapper;

import com.app.restful.domain.dto.PostDTO;
import com.app.restful.domain.vo.PostVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
//    모든 게시글 들 불러오기
    public List<PostDTO> selectAll();

//    게시글 상세 불러오기
    public PostDTO select(long id);

//    게시글 작성하기
    public void insert(PostVO postVO);

//    게시글 수정하기
    public void update(PostVO postVO);

//    게시글 삭제하기
    public void delete(long id);

//    회원 탈퇴 시 회원이 작성 한 게시글 일괄 삭제
    public void deleteByMemberId(Long memberId);
}

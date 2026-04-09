package com.app.threetier.mapper;

import com.app.threetier.domain.dto.PostDTO;
import com.app.threetier.domain.vo.PostVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PostMapper {
//    게시글 전체 혹은 단일 조회
    public List<PostDTO> selectAll();
    public Optional<PostDTO> select(Long id);

//    게시글 수정
    public void update(PostVO postVO);

//    게시글 조회수 증가
    public void updateReadCount(Long id);

//    게시글 삭제
    public void delete(Long id);
}

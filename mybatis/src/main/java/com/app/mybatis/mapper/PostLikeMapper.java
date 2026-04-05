package com.app.mybatis.mapper;

import com.app.mybatis.domain.dto.PostDTO;
import com.app.mybatis.domain.vo.PostLikeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Mapper
public interface PostLikeMapper {
    void insert(PostLikeVO postLikeVO);
    void delete(PostLikeVO postLikeVO);
    List<PostDTO> selectAll(Long memberId);
    List<PostDTO> selectAllWithKeyword(HashMap<String, Object> params);
    Optional<PostDTO> selectByIdAndPostLike(HashMap<String, Object> params);
    List<PostDTO> selectAllAndIsClick(@Param("memberId") Long memberId);
}

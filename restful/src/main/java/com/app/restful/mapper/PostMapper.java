package com.app.restful.mapper;

import com.app.restful.domain.dto.PostDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
//    모든 게시글 들 불러오기
    public List<PostDTO> selectAll();

//    게시글 상세 불러오기
    public PostDTO select(long id);
}

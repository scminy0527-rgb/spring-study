package com.app.mybatis.mapper;

import com.app.mybatis.domain.dto.PostCountDTO;
import com.app.mybatis.domain.dto.PostDTO;
import com.app.mybatis.domain.vo.PostVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

@Mapper
public interface PostMapper {
    public void insert(PostVO postVO);
    public List<PostDTO> selectAll();
    public Optional<PostDTO> selectById(Long id);
    public Long selectPostReadCountById(Long id);
    public void update(PostVO postVO);
    public void delete(Long id);
    public void updatePostReadCount(Long id);
    public List<PostDTO> selectAllWithOrder(HashMap<String, Object> orders);

    public PostCountDTO selectTotalPostCountAndTotalPageCount(int limit);
    public PostCountDTO selectNewTotalPostCountAndTotalPageCount(HashMap<String, Object> orders);
    public List<PostCountDTO> selectByKeyword(String keyword);
}

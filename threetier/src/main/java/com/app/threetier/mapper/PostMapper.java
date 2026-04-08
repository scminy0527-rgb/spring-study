package com.app.threetier.mapper;

import com.app.threetier.domain.dto.PostDTO;
import com.app.threetier.domain.vo.PostVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PostMapper {
    public List<PostDTO> selectAll();
    public Optional<PostDTO> select(Long id);
    public void update(PostVO postVO);
    public void delete(Long id);
}

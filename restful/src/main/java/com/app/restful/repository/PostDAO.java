package com.app.restful.repository;

import com.app.restful.domain.dto.PostDTO;
import com.app.restful.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostDAO {
    private final PostMapper postMapper;

//    모든 게시글 들 불러오기
    public List<PostDTO> findAll() {
        return postMapper.selectAll();
    }

//    게시글 상세 불러오기
    public Optional<PostDTO> findById(long id) {
        return Optional.ofNullable(postMapper.select(id));
    }
}

package com.app.threetier.repository;

// 레포지토리(Repository)
// 엔티티에 의해 생성된 데이터베이스 테이블에 직접 접근하는 메서드를 가진 객체 또는 인터페이스 (ORM 에 대한 설명)
//DAO 는 객체지향 아닌거를 객체지향처럼 보이게 하기

import com.app.threetier.domain.dto.PostDTO;
import com.app.threetier.domain.vo.PostVO;
import com.app.threetier.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostDAO {
    private final PostMapper postMapper;

    public List<PostDTO> findAll() {
        return postMapper.selectAll();
    }

    public Optional<PostDTO> findById(Long id){
        return postMapper.select(id);
    }

//    게시글 수정
    public void update(PostVO postVO){
        postMapper.update(postVO);
    }

//    수정 삭제는 그대로 update, delete 로 쓰는게 fm 관행
//    게시글 조회수 증가
    public void updateReadCount(Long id){
        postMapper.updateReadCount(id);
    }

    public void delete(Long id){
        postMapper.delete(id);
    }
}

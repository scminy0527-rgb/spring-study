package com.app.threetier.service;

import com.app.threetier.domain.dto.PostDTO;
import com.app.threetier.domain.vo.PostVO;

import java.util.List;
import java.util.Optional;

// 서비스의 원칙
//1개의 서비스에는 여러개의 쿼리가 발생한다. 트랜젝션 관리가 필요
//2. 구현체의 분리를 통해 독립적으로 보다 자유로운 확장성을 보장하고
//유지보수를 증가시키기 위해 인터페이스로 설계한다
//3. 서비스 에는 해당 서비스의 이름이 명확히 드러나야 한다.
public interface PostService {
    public List<PostDTO> getPosts();
    public PostDTO getPost(Long id);
    public void updatePost(PostVO postVO);
    public void deletePost(Long id);
}

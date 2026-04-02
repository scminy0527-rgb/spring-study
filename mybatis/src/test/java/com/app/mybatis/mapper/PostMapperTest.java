package com.app.mybatis.mapper;

import com.app.mybatis.domain.dto.PostDTO;
import com.app.mybatis.domain.vo.PostVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class PostMapperTest {

    @Autowired
    private PostMapper postMapper;

    @Test
    public void insertTest(){
        PostVO postVO = new PostVO();
        postVO.setPostTitle("안녕하세요?");
        postVO.setPostContent("오늘도 반가운 하루 보내세요");
        postVO.setMemberId(24L);

        postMapper.insert(postVO);
    }

    @Test
    public void selectAllTest(){
        log.info("{}",postMapper.selectAll());
    }

    @Test
//    그리고 글을 읽으면 동시에 조회수도 1 증가 하도록 해야함
    public void selectByIdTest(){
        log.info("dto 테스트");
        postMapper.selectById(2L).map(PostDTO::toString).ifPresent(log::info);
        postMapper.updatePostReadCount(2L);
    }

    @Test
    public void selectPostReadCountTest(){
        log.info("조회수 조회 테스트");
        log.info("게시글 조회 수: {}", postMapper.selectPostReadCountById(4L));
    }

    @Test
    public void updateTest(){
        PostVO postVO = new PostVO();
        postVO.setId(1L);
        postVO.setPostTitle("추가적인 테스트");
        postVO.setPostContent("안녕하세요???");

        postMapper.update(postVO);
    }

    @Test
    public void deleteTest(){
        postMapper.delete(3L);
    }

    @Test
    public void updateReadCountTest(){
        postMapper.updatePostReadCount(2L);
    }
}

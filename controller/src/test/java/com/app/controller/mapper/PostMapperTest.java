package com.app.controller.mapper;

import com.app.controller.domain.dto.PostDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class PostMapperTest {
    @Autowired
    private PostMapper postMapper;

    @Test
    public void testSelectAll() {
        List<PostDTO> posts = postMapper.selectAll();
        for (PostDTO post : posts) {
            log.info(post.toString());
        }
    }
}

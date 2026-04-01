package com.app.mybatis.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class MapperTest {
    @Autowired
    private TimeMapper timeMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Test
    public void mapperTest() {
        log.info(timeMapper.getTime());
        log.info(timeMapper.getTime2());

        log.info("{}",memberMapper.selectAll());
    }
}

package com.app.mybatis.mapper;

import com.app.mybatis.domain.vo.MemberVO;
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



    @Test
    public void mapperTest4(){
//        여기서 map 은 optional 맵 이다
        memberMapper.select(1L).map(MemberVO::toString).ifPresent(log::info);
    }

    @Test
    public void mapperTest5(){
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberName("이순신");
        memberVO.setMemberEmail("test456@gmail.com");
        memberVO.setMemberPassword("test123!@#");

        memberMapper.insert(memberVO);
    }

    @Test
    public void updateTest6(){
        MemberVO memberVO = new MemberVO();
        memberVO.setId(23l);
        memberVO.setMemberName("장길동");
        memberVO.setMemberEmail("test456@gmail.com");
        memberVO.setMemberPassword("test123!@#");

        memberMapper.select(23l);
        memberMapper.update(memberVO);
        memberMapper.select(23l);
    }

    @Test
    public void deleteTest(){
        memberMapper.delete(23l);
    }

}

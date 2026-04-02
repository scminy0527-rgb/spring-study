package com.app.mybatis.mapper;

import com.app.mybatis.domain.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberMapper {
    public List<MemberVO> selectAll();
    public Optional<MemberVO> select(Long id);
    public void insert(MemberVO memberVO);
    public void update(MemberVO memberVO);
    public void delete(Long id);
}

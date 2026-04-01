package com.app.mybatis.mapper;

import com.app.mybatis.domain.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
    public List<MemberVO> selectAll();
}

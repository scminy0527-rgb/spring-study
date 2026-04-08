package com.app.controller_pr.mapper;

import com.app.controller_pr.domain.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {
    public void join(MemberVO memberVO);
    public Optional<MemberVO> selectOneByEmeberEmailAndMemberPassword(MemberVO memberVO);
}

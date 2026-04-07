package com.app.controller.mapper;

import com.app.controller.domain.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {
    public void insert(MemberVO memberVO);
    public int existByMemberEmail(String memberEmail);
    public Optional<MemberVO> selectByMemberEmailAndMemberPassword(MemberVO memberVO);
    public void update(MemberVO memberVO);
    public void delete(Long id);
    public int existByMemberIdAndMemberEmailForUpdate(MemberVO memberVO);
}

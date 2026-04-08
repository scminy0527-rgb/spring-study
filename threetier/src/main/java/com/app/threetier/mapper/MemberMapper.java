package com.app.threetier.mapper;

import com.app.threetier.domain.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {
    public void insert(MemberVO memberVO);
    public int existByMemberEmail(String memberEmail);
    public Optional<MemberVO> selectByMemberEmailAndMemberPassword(MemberVO memberVO);
    public Optional<MemberVO> selectOneById(Long id);
    public void update(MemberVO memberVO);
    public void delete(Long id);
    public int existByMemberIdAndMemberEmailForUpdate(MemberVO memberVO);
}

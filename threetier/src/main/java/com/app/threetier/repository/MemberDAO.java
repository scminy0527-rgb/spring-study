package com.app.threetier.repository;

import com.app.threetier.domain.vo.MemberVO;
import com.app.threetier.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberDAO {
    private final MemberMapper memberMapper;

    public void join(MemberVO memberVO){
        memberMapper.insert(memberVO);
    }

    public Optional<MemberVO> findByMemberEmailAndMemberPassword(MemberVO memberVO){
        return memberMapper.selectByMemberEmailAndMemberPassword(memberVO);
    }
}

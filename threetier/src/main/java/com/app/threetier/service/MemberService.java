package com.app.threetier.service;


import com.app.threetier.domain.vo.MemberVO;

public interface MemberService {
    public void joinMember(MemberVO memberVO);
    public MemberVO loginMember(MemberVO memberVO);
}

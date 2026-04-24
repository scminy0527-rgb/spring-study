package com.app.oauth.mapper;

import com.app.oauth.domain.dto.MemberDTO;
import com.app.oauth.domain.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
//    가입
    public void insert(MemberVO memberVO);

//    아이디로 멤버 가져오기
    public MemberDTO select(Long id);

//    가입 여부 조회
    public boolean existMemberByMemberEmail(String memberEmail);

//    아이디 비밀번호로 조회
    public MemberDTO selectByMemberEmail(String memberEmail);

//    회원 수정
    public void update(MemberVO memberVO);

//    회원 탈되
    public void delete(Long id);
}

package com.app.restful.mapper;

import com.app.restful.domain.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
//    회원 추가
    public void insert(MemberVO memberVO);

//    이메일 중복 여부 확인
    public int existByMemberEmail(String memberEmail);

//    이메일 비밀번호 로그인
    public MemberVO selectByMemberEmailAndMemberPassword(MemberVO memberVO);

//    아이디로 회원 찾기
    public MemberVO select(Long id);

//    멤버 전체 조회
    public List<MemberVO> selectAll();

//    회원 정보 수정
    public void update(MemberVO memberVO);

//    회원 탈퇴
    public void delete(Long id);
    public int existByMemberIdAndMemberEmailForUpdate(MemberVO memberVO);
}

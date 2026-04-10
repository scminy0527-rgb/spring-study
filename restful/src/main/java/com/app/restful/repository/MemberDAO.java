package com.app.restful.repository;

import com.app.restful.domain.vo.MemberVO;
import com.app.restful.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberDAO {
    private final MemberMapper memberMapper;

//    멤버 추가
    public void save(MemberVO memberVO){
        memberMapper.insert(memberVO);
    }

//    이메일 토대로 중복 확인
    public int existByEmail(String memberEmail){
        return memberMapper.existByMemberEmail(memberEmail);
    }

//    로그인 하기
    public MemberVO findByEmailAndPassword(MemberVO memberVO){
        return memberMapper.selectByMemberEmailAndMemberPassword(memberVO);
    }

//    아이디 토대로 한명 가져오기
    public MemberVO findById(Long id){
        return memberMapper.select(id);
    }

//    멤버 전체 조회
    public List<MemberVO> findAll(){
        return memberMapper.selectAll();
    }

//    멤버 정보 수정
    public void update(MemberVO memberVO){
        memberMapper.update(memberVO);
    }

//    멤버 삭제
    public void delete(Long id){
        memberMapper.delete(id);
    }
}

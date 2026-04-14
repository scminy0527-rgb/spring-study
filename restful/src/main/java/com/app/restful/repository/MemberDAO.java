package com.app.restful.repository;

import com.app.restful.domain.dto.MemberJoinRequestDTO;
import com.app.restful.domain.vo.MemberVO;
import com.app.restful.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


// 원래 여기서 optional 처리 해야 함 (공식임)
// 객체지향 스럽지 않는것을 객체지향회ㅏ

@Repository
@RequiredArgsConstructor
public class MemberDAO {
    private final MemberMapper memberMapper;

//    멤버 추가
    public void join(MemberVO memberVO) {
        memberMapper.insert(memberVO);
    }

//    이메일 토대로 중복 확인 (이메일 유무 검사)
    public int existByEmail(String memberEmail){
        return memberMapper.existByMemberEmail(memberEmail);
    }

//    로그인 하기 (여기서 optional 감싸야 함)
    public Optional<MemberVO> findByEmailAndPassword(MemberVO memberVO){
        return Optional.ofNullable(memberMapper.selectByMemberEmailAndMemberPassword(memberVO));
    }

//    아이디 토대로 한명 가져오기
    public Optional<MemberVO> findById(Long id){
        return Optional.ofNullable(memberMapper.select(id));
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

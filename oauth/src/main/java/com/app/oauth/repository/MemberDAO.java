package com.app.oauth.repository;

import com.app.oauth.domain.dto.MemberDTO;
import com.app.oauth.domain.vo.MemberVO;
import com.app.oauth.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberDAO {
    private final MemberMapper memberMapper;

//    회원 추가
    public void save(MemberVO memberVO) {
        memberMapper.insert(memberVO);
    }

//    회원 찾기
    public Optional<MemberVO> findById(Long id) {
        return  Optional.ofNullable(memberMapper.select(id));
    }


//    중복 여부
    public boolean existsByMemberEmail(String memberEmail) {
        return  memberMapper.existMemberByMemberEmail(memberEmail);
    }

//    로그인
    public Optional<MemberDTO> findByMemberEmail(String memberEmail) {
        return Optional.ofNullable(memberMapper.selectByMemberEmail(memberEmail));
    }

//    회원 정보 수정
    public void update(MemberVO memberVO) {
        memberMapper.update(memberVO);
    }

//    회원 삭제
    public void delete(Long id) {
        memberMapper.delete(id);
    }
}

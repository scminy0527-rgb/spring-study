package com.app.oauth.repository;

import com.app.oauth.domain.vo.SocialMemberVO;
import com.app.oauth.mapper.SocialMemberMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Data
public class SocialMemberDAO {
    private final SocialMemberMapper socialMemberMapper;

//    소셜 회원 추가
    public void save(SocialMemberVO socialMemberVO) {
        socialMemberMapper.insert(socialMemberVO);
    }
}

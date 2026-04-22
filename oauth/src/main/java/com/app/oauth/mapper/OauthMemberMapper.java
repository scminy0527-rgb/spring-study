package com.app.oauth.mapper;

import com.app.oauth.domain.vo.OauthMemberVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OauthMemberMapper {
//    회원 가입
    public void insert(OauthMemberVO oauthMemberVO);

//    모든 회원 조회
    public List<OauthMemberVO> selectAll();

//    아이디로 단일 회원 조회
    public OauthMemberVO select(Long id);

//    회원 수정
    public void update(OauthMemberVO oauthMemberVO);

//    회원 삭제
    public void  delete(Long id);
}

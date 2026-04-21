package com.app.practice.mapper;

import com.app.practice.domain.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
//    유저 회원가입
    public void insert(UserVO userVO);

//    유저 이메일 중복여부 확인
    public int existByUserEmail(String email);

//    아이디로 유저 찾기
    public UserVO selectOne(Long id);

//    로그인 하기
    public UserVO selectByEmailAndPassword(UserVO userVO);

//    유저 전체 조회
    public List<UserVO> selectAll();

//    유저 정보 수정하기
    public void update(UserVO userVO);
}

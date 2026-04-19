package com.app.rest_pr.mapper;

import com.app.rest_pr.domain.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
//    회원가입 하기
    public void insert(UserVO userVO);

//    이메일 중복여부 확인
    public int existByEmail(String email);

//    모든 유저 불러오기
    public List<UserVO> selectAll();

//    로그인 하기
    public UserVO selectByEmailAndPassword(UserVO userVO);

//    유저 한명 조회하기
    public UserVO selectOne(Long id);

//    유저 정보 수정하기
    public void update(UserVO userVO);

    public void delete(Long id);
}

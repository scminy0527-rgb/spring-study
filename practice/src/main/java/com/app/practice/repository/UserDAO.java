package com.app.practice.repository;

import com.app.practice.domain.vo.UserVO;
import com.app.practice.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDAO {
    private final UserMapper userMapper;

//    유저 회원가입
    public void join(UserVO userVO){
        userMapper.insert(userVO);
    }

//    유저 이메일 중복여부 확인
    public int existByUserEmail(String userEmail){
        return userMapper.existByUserEmail(userEmail);
    }

//    아이디로 유저 찾기
    public Optional<UserVO> findById(Long id){
        UserVO userVO = userMapper.selectOne(id);
        return Optional.ofNullable(userVO);
    }

//    로그인 하기
    public Optional<UserVO> findByEmailAndPassword(UserVO userVO){
        UserVO user = userMapper.selectByEmailAndPassword(userVO);
        return Optional.ofNullable(user);
    }

//    유저 정보 수정하기
    public void update(UserVO userVO){
        userMapper.update(userVO);
    }
}

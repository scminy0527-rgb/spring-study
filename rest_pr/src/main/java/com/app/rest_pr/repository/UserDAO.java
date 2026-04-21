package com.app.rest_pr.repository;

import com.app.rest_pr.domain.dto.UserResponseDTO;
import com.app.rest_pr.domain.vo.UserVO;
import com.app.rest_pr.mapper.UserMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDAO {
    private final UserMapper userMapper;

//    유저 회원 가입
    public void save(UserVO userVO){
        userMapper.insert(userVO);
    }

//    유저 이메일 중복 여부 확인
    public int existByEmail(String email){
        return userMapper.existByEmail(email);
    }

//    전체 유저 조회
    public List<UserVO> findAll(String order){
        return userMapper.selectAll(order);
    }

//    로그인 하기
    public Optional<UserVO> findByEmailAndPassword(UserVO userVO){
        UserVO user = userMapper.selectByEmailAndPassword(userVO);
        return Optional.ofNullable(user);
    }

//    유저 한명 조회하기
    public Optional<UserVO> findById(Long id){
        UserVO userVO = userMapper.selectOne(id);
        return Optional.ofNullable(userVO);
    }

//    유저 정보 수정하기
    public void update(UserVO userVO){
        userMapper.update(userVO);
    }

//    유저 탈퇴하기
    public void delete(Long id){
        userMapper.delete(id);
    }
}

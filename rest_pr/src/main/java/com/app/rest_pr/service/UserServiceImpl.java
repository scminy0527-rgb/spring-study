package com.app.rest_pr.service;

import com.app.rest_pr.domain.dto.UserJoinRequestDTO;
import com.app.rest_pr.domain.dto.UserLoginRequestDTO;
import com.app.rest_pr.domain.dto.UserResponseDTO;
import com.app.rest_pr.domain.dto.UserUpdateRequestDTO;
import com.app.rest_pr.domain.vo.UserVO;
import com.app.rest_pr.exceptions.MemberException;
import com.app.rest_pr.repository.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor =  Exception.class)
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;

//    유저 회원가입 하는 서비스
    @Override
    public void registerUser(UserJoinRequestDTO userJoinRequestDTO) {
        this.checkEmailDuplicate(userJoinRequestDTO.getUserEmail());
        userDAO.save(UserVO.from(userJoinRequestDTO));
    }

//    이메일 중복 확인하는 서비스 정의
    @Override
    public void checkEmailDuplicate(String email) {
        if(userDAO.existByEmail(email) != 0){
            throw new MemberException("해당 이메일은 이미 사용중인 이메일 입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<UserResponseDTO> userList = userDAO.findAll().stream()
                .map(UserResponseDTO::from)
                .collect(Collectors.toList());

        return userList;
    }

//    유저 로그인을 하는 함수
    @Override
    public UserResponseDTO loginUser(UserLoginRequestDTO userLoginRequestDTO) {
//        db에 req 을 날려서 가져온 user 데이터
        UserVO userVO = userDAO
                .findByEmailAndPassword(UserVO.from(userLoginRequestDTO))
                .orElseThrow(() -> {
            throw new MemberException("로그인 실패 하였습니다.", HttpStatus.BAD_REQUEST);
        });
        return  UserResponseDTO.from(userVO);
    }

//    유저 번호(아이디) 를 토대로 유저 불러오는 함수
    @Override
    public UserResponseDTO getUserInfo(Long id) {
        UserVO userVO = userDAO.findById(id).orElseThrow(() -> {
            throw new MemberException("해당 유저는 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        });
        return UserResponseDTO.from(userVO);
    }

//    유저 정보를 수정하는 서비스
    @Override
    public void updateUserInfo(Long id, UserUpdateRequestDTO userUpdateRequestDTO) {
        UserVO userVO = UserVO.from(userUpdateRequestDTO);
        userVO.setId(id);
        userDAO.update(userVO);
    }

//    유저 탈퇴 서비스
    @Override
    public void withdrawUser(Long id) {
        userDAO.delete(id);
    }
}

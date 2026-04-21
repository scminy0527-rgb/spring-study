package com.app.practice.service;

import com.app.practice.domain.dto.UserJoinRequestDTO;
import com.app.practice.domain.dto.UserLoginRequestDTO;
import com.app.practice.domain.dto.UserResponseDTO;
import com.app.practice.domain.dto.UserUpdateRequestDTO;
import com.app.practice.domain.vo.UserVO;
import com.app.practice.exception.UserException;
import com.app.practice.repository.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;

//    유저 회원가입 서비스
    @Override
    public void registerUser(UserJoinRequestDTO userJoinRequestDTO) {
        String userEmail = userJoinRequestDTO.getUserEmail();
        this.checkEmailDuplicate(userEmail);
        userDAO.join(UserVO.from(userJoinRequestDTO));
    }

//    유저 이메일 중복 체크 서비스 (회원가입시 활용)
    @Override
    public void checkEmailDuplicate(String userEmail) {
        if(userDAO.existByUserEmail(userEmail) != 0){
            throw new UserException("이메일 중복 입니다.", HttpStatus.BAD_REQUEST);
        }
    }

//    아이디로 유저 찾기
    @Override
    public UserResponseDTO getUserById(Long id) {
        return userDAO.findById(id)
                .map(UserResponseDTO::from)
                .orElseThrow(() -> {
                    throw new UserException("해당 유저가 없습니다.", HttpStatus.NOT_FOUND);
                });
    }

//    유저 로그인 하는 서비스
    @Override
    public UserResponseDTO loginUser(UserLoginRequestDTO userLoginRequestDTO) {
        return userDAO.findByEmailAndPassword(UserVO.from(userLoginRequestDTO))
                .map(UserResponseDTO::from)
                .orElseThrow(() -> {
                    throw new UserException("로그인 실패하였습니다.", HttpStatus.BAD_REQUEST);
                });
    }

//    유저 정보 수정하는 서비스
    @Override
    public void modifyUserInfo(Long id, UserUpdateRequestDTO userUpdateRequestDTO) {
        UserVO userVO = UserVO.from(userUpdateRequestDTO);
        userVO.setId(id);
        userDAO.update(userVO);
    }
}

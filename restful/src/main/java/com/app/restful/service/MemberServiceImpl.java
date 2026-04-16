package com.app.restful.service;

import com.app.restful.domain.dto.MemberJoinRequestDTO;
import com.app.restful.domain.dto.MemberLoginRequestDTO;
import com.app.restful.domain.dto.MemberResponseDTO;
import com.app.restful.domain.dto.MemberUpdateRequestDTO;
import com.app.restful.domain.vo.MemberVO;
import com.app.restful.exceptions.DuplicateEmailException;
import com.app.restful.exceptions.MemberException;
import com.app.restful.exceptions.MemberLoginException;
import com.app.restful.repository.MemberDAO;
import com.app.restful.repository.PostDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor =  Exception.class)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberDAO memberDAO;
    private final PostDAO postDAO;

    @Override
    public void join(MemberJoinRequestDTO memberJoinRequestDTO) {
//        이메일 중복 여부 확인
//        서비스 단에서 DTO 를 VO 로 옮겨 담는다
        this.checkEmailDuplicate(memberJoinRequestDTO.getMemberEmail());
        memberDAO.join(MemberVO.from(memberJoinRequestDTO));
    }

//    이메일 중복 여부 검사 하는 함수
//    해당 이메일로 된 계정이 몇개 있냐 확인
//    값이 0 이 아니면 해당 이메일은 중복 이메일
//    그런데 그냥 void 형태로 놔두고 던지는거도 가능
    @Override
    public void checkEmailDuplicate(String email) {
        if(memberDAO.existByEmail(email) != 0){
            throw new MemberException("이메일 중복입니다.", HttpStatus.CONFLICT);
        }
    }

    @Override
    public void checkMemberIdExists(Long id) {
        if(!memberDAO.findById(id).isPresent()){
            throw new MemberException("해당 멤버가 존재하지 않습니다.",  HttpStatus.UNAUTHORIZED);
        }
    }

//    로그인 서비스 시에는 화면으로 비밀번호를 절 때 주면 안된다
//    아이디 또는 비밀번호가 일치 하지 않으면 예외를 던져야 한다
//    정석대로 하려면 throw 는 서비스 단 에서 던져야 함
    @Override
    public MemberResponseDTO login(MemberLoginRequestDTO memberLoginRequestDTO) {
        return memberDAO
                .findByEmailAndPassword(MemberVO.from(memberLoginRequestDTO))
                .map(MemberResponseDTO::from)
                .orElseThrow(() -> {
                    throw new MemberLoginException("아이디 또는 비밀번호를 확인하세요");
                });
    }

    @Override
    public void checkUserExist(MemberVO memberVO) {
        Optional<MemberVO> foundMember = Optional.ofNullable(memberVO);
        if (!foundMember.isPresent()) {
            throw new MemberLoginException("로그인 실패");
        }
    }

//    회원 정보 조회 서비스
    @Override
    public MemberResponseDTO getMemberInfo(Long id) {
//        회원 비밀번호 제거해야 하는 서비스
        return memberDAO.findById(id)
                .map(MemberResponseDTO::from)
                .orElseThrow(() -> {throw new MemberException("해당 회원을 찾을 수 없습니다.",   HttpStatus.BAD_REQUEST); });
    }

//    회원 목록을 가져오는 서비스
    @Override
    public List<MemberResponseDTO> getMembers() {
        List<MemberVO> memberVOList = memberDAO.findAll();

//        해당 과정을 통해서 리스트 내 원소를 MemberResponseDTO 자료형으로 바꾼 새로운 리스트를 만들어서 반환
//        비밀번호 제거
        List<MemberResponseDTO> members = memberVOList.stream()
                .map(memberVO -> MemberResponseDTO.from(memberVO))
                .collect(Collectors.toList());

        return members;
    }

//    회원 정보 수정을 하는 서비스
//    하기 전 이메일 중복 여부도 체크 해야 함
    @Override
    public void modifyMemberInfo(MemberUpdateRequestDTO memberUpdateRequestDTO) {
//        만약 바꾸려고 하는 이메일이 중복 이면 에러 방출해야함
        memberDAO.update(MemberVO.from(memberUpdateRequestDTO));
    }

//    회원 탈퇴를 하는 서비스
    @Override
    public void withdrawMember(Long id) {
//        참조하는 포스트 게시판 삭제 해야함
        postDAO.deleteByMemberId(id);
        memberDAO.delete(id);
    }
}

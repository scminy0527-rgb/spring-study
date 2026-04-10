package com.app.restful.service;

import com.app.restful.domain.dto.MemberResponseDTO;
import com.app.restful.domain.vo.MemberVO;
import com.app.restful.repository.MemberDAO;
import lombok.RequiredArgsConstructor;
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

    @Override
    public void join(MemberVO memberVO) {

    }

    @Override
    public Optional<MemberVO> login(MemberVO memberVO) {
        return Optional.empty();
    }

//    회원 정보 조회 서비스
    @Override
    public Optional<MemberResponseDTO> getMemberInfo(Long id) {
//        회원 비밀번호 제거해야 하는 서비스
        MemberVO foundMember = memberDAO.findById(id);

//        비밀번호 필드가 없는 새로운 DTO 에 담아서 안전하게 반환
        MemberResponseDTO memberResponseDTO = MemberResponseDTO.from(foundMember);
        return Optional.ofNullable(memberResponseDTO);
    }

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
}

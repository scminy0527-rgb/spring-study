package com.app.threetier.service;

import com.app.threetier.domain.vo.MemberVO;
import com.app.threetier.exception.MemberException;
import com.app.threetier.repository.MemberDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class})
public class MemberServiceImpl implements MemberService {
    private final MemberDAO memberDAO;
    @Override
    public void joinMember(MemberVO memberVO) {
        memberDAO.join(memberVO);
    }

    @Override
    public MemberVO loginMember(MemberVO memberVO) {
        return memberDAO.findByMemberEmailAndMemberPassword(memberVO)
                .orElseThrow(() -> new MemberException("해당 멤버가 존재하지 않습니다."));
    }
}

package com.app.restful.domain.vo;

import com.app.restful.domain.dto.MemberJoinRequestDTO;
import com.app.restful.domain.dto.MemberLoginRequestDTO;
import com.app.restful.domain.dto.MemberUpdateRequestDTO;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

//json 으로 직렬화 하기 위한 거 필요
@Component
@Data
public class MemberVO implements Serializable {
    private Long id;
    private String memberEmail;
    private String memberPassword;
    private String memberName;

//    화면에서 받은 req DTO 에 있는 정보를 VO 로 옮겨담는 정적 팩토리 매서드
    public static MemberVO from (MemberJoinRequestDTO memberJoinRequestDTO) {
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberName(memberJoinRequestDTO.getMemberName());
        memberVO.setMemberEmail(memberJoinRequestDTO.getMemberEmail());
        memberVO.setMemberPassword(memberJoinRequestDTO.getMemberPassword());
        return memberVO;
    }

    public static MemberVO from (MemberLoginRequestDTO memberLoginRequestDTO) {
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberEmail(memberLoginRequestDTO.getMemberEmail());
        memberVO.setMemberPassword(memberLoginRequestDTO.getMemberPassword());
        return memberVO;
    }

    public static MemberVO from (MemberUpdateRequestDTO memberUpdateRequestDTO) {
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberName(memberUpdateRequestDTO.getMemberName());
        memberVO.setMemberPassword(memberUpdateRequestDTO.getMemberPassword());

        return memberVO;
    }
}

package com.app.oauth.domain.vo;

import com.app.oauth.domain.dto.MemberDTO;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class MemberVO {
    private Long id;
    private String memberEmail;
    private String memberPassword;
    private String memberPicture;
    private String memberName;
    private String memberNickname;

    {
//        초기화 블럭 (DEFAULT)
        this.setMemberPicture("default.png");
        this.setMemberNickname("개복치 1단계");
    }

    public static MemberVO from(MemberDTO memberDTO) {
        MemberVO memberVO = new MemberVO();
        memberVO.setId(memberDTO.getId());
        memberVO.setMemberEmail(memberDTO.getMemberEmail());
        memberVO.setMemberPassword(memberDTO.getMemberPassword());
        memberVO.setMemberPicture(
                memberDTO.getMemberPicture() != null
                        ? memberDTO.getMemberPicture()
                        : "default.jpg"
        );
        memberVO.setMemberName(memberDTO.getMemberName());
        memberVO.setMemberNickname(memberDTO.getMemberNickname());

        return  memberVO;
    }
}

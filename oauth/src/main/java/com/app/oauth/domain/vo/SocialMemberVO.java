package com.app.oauth.domain.vo;

import com.app.oauth.domain.dto.MemberDTO;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class SocialMemberVO {
    private Long id;
    private String socialMemberProviderId;
    private String socialMemberProvider;
    private Long memberId;

//    동적 쿼리가 좀 더 가벼워지는 현상 존재
    {
        //        초기화 블럭 (DEFAULT)
        this.setSocialMemberProvider("local");
    }

    public static SocialMemberVO from(MemberDTO memberDTO) {
        SocialMemberVO socialMemberVO = new SocialMemberVO();
        socialMemberVO.setId(memberDTO.getId());
        socialMemberVO.setSocialMemberProviderId(memberDTO.getSocialMemberProviderId());
        socialMemberVO.setSocialMemberProvider(
                memberDTO.getSocialMemberProvider() != null
                        ? memberDTO.getSocialMemberProvider()
                        : "local"
        );
        socialMemberVO.setMemberId(memberDTO.getMemberId());

        return socialMemberVO;
    }
}

package com.app.oauth.domain.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

// 서비스 쪽에서 다 전달 받아야 하는 값

@Component
@Data
public class MemberDTO {
    private Long id;
    private String memberEmail;
    private String memberPassword;
    private String memberPicture;
    private String memberName;
    private String memberNickname;
    private String socialMemberProviderId;
    private String socialMemberProvider;
    private Long memberId;
}

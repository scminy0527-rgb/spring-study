package com.app.oauth.api;

import com.app.oauth.domain.dto.MemberDTO;
import com.app.oauth.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberAPI {
    private final MemberService memberService;

//    일반 로그인의 회원가입 (소셜 로그인은 시큐리티 부분에서 따로 해야함)
    @PostMapping("/join")
    public void join(@RequestBody MemberDTO memberDTO) {
        memberService.join(memberDTO);
    }
}

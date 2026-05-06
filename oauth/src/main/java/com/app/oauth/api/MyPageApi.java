package com.app.oauth.api;

import com.app.oauth.domain.dto.MemberDTO;
import com.app.oauth.domain.dto.response.ApiResponseDTO;
import com.app.oauth.domain.vo.MemberVO;
import com.app.oauth.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/private")
@RequiredArgsConstructor
@Slf4j
public class MyPageApi {

    private final MemberService memberService;

    @PostMapping("my-page-test")
    public void test(Authentication authentication) {
        log.info("토큰 private 테스트 : {}", authentication.getPrincipal().toString());
    }

    // POST /private/api/members/update-picture
    // Authentication.getPrincipal() → JwtAuthenticationFilter가 설정한 MemberDTO (id 포함)
    @PostMapping("/api/members/update-picture")
    public ResponseEntity<ApiResponseDTO> updatePicture(
            @RequestBody Map<String, String> body,
            Authentication authentication
    ) {
        MemberDTO memberDTO = (MemberDTO) authentication.getPrincipal();
        MemberVO memberVO = new MemberVO();
        memberVO.setId(memberDTO.getId());
        memberVO.setMemberPicture(body.get("memberPicture"));
        return ResponseEntity.ok(memberService.updatePicture(memberVO));
    }
}

package com.app.oauth.api;

import com.app.oauth.domain.dto.response.ApiResponseDTO;
import com.app.oauth.domain.dto.JwtTokenDTO;
import com.app.oauth.domain.dto.MemberDTO;
import com.app.oauth.service.MemberService;
import com.app.oauth.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberAPI {
    private final MemberService memberService;
    private final JwtTokenUtil jwtTokenUtil;

//    일반 로그인의 회원가입 (소셜 로그인은 시큐리티 부분에서 따로 해야함)
    @PostMapping("/join")
    public ResponseEntity<ApiResponseDTO> join(@RequestBody MemberDTO memberDTO) {
//        회원가입을 하면 가입 결과를 반환한다.
        ApiResponseDTO result = memberService.join(memberDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/token/claims")
    public ResponseEntity<ApiResponseDTO> tokenClaims(
            @CookieValue(value = "accessToken", required = false) String token) {
        Claims claims = jwtTokenUtil.parseToken(token);
        String memberEmail = claims.get("memberEmail").toString();
        String provider = claims.get("provider", String.class);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.of(true, "토큰 파싱 성공", claims));
    }

//    조회 하는 me 매서드 정의 해야 함
//    토큰을 parse 한 뒤에 안에 있는 유저 id 정보를 토대로 유저 정보 가져오는
//    쿼리 실행 후 해당 유저 정보를 응답 DTO 에 담아서 응답하는 컨트롤러
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDTO> me(
            @CookieValue(value = "accessToken") String token
    ) {
        ApiResponseDTO result = memberService.me(token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

//    private 로 시작하는 모든 경로 테스트

}

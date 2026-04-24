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

//    이건 나중에 옮겨야 함
//    @PostMapping("/login")
//    public ResponseEntity<ApiResponseDTO> login(@RequestBody MemberDTO memberDTO, HttpServletResponse response) {
//        JwtTokenDTO result = memberService.login(memberDTO);
////        여기서 토큰을 꺼내서 쿠키에 심어야 함 (쿠키는 키 밸류 형태)
////        Cookie accessTokenCookie = new Cookie("accessToken", result.getAccessToken());
////        보안을 위해서 무조건 넣어야하는 옵션
////        accessTokenCookie.setHttpOnly(true);
//
//        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", result.getAccessToken())
//                .httpOnly(true)
//                .sameSite("Lax")   // CSRF 방어
//                .path("/")
//                .secure(false)  // 개발 에서는 false, 배포 에서는 true
//                .maxAge(60 * 60 * 24)
//                .build();
//
//        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", result.getAccessToken())
//                .httpOnly(true)
//                .sameSite("Lax")   // CSRF 방어
//                .path("/")  // 개발 에서는 false, 배포 에서는 true
//                .secure(false)
//                .maxAge(60 * 60 * 24)
//                .build();
//
//        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
//        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
//                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
//                .body(ApiResponseDTO.of(true, "로그인 성공"));
//    }

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
//    @

//    조회 하는 me 매서드 정의 해야 함
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDTO> me(@CookieValue(value = "accessToken") String token) {
        ApiResponseDTO result = memberService.me(token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }
}

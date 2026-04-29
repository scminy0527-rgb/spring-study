package com.app.oauth.handler;

import com.app.oauth.domain.dto.JwtTokenDTO;
import com.app.oauth.domain.dto.MemberDTO;
import com.app.oauth.domain.dto.response.ApiResponseDTO;
import com.app.oauth.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class Oauth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthService authService;

//    Spring Security가 소셜 로그인 성공을 감지하면 자동으로 호출해주는 메서드
//    매서드의 매개 변수 에는 요청 및 응답객체, 그리고 인증 객체가 존재
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

//        인증 객체 중에 본질이 OAuth2AuthenticationToken 타입인 객체만 선택 (oauth 를 수행하기 위해)
//        Authentication 는 각 인증 종류의 부모 클래스
//        여기서는 OAuth2AuthenticationToken 와 관련된 기능을 사용하기 위해 다운캐스팅 및 authToken 라는 변수로 캐스팅
//        다형성
//        if 문을 통해서 oauth2 인증 일때만 하위 기능들이 수행되도록 하기
        if(authentication instanceof OAuth2AuthenticationToken authToken){
//            사용자 정보
            OAuth2User oauth2User = authToken.getPrincipal();
            Map<String, Object> attributes = oauth2User.getAttributes();

//            oauth2 서비스 제공자
            String socialMemberProvider = authToken.getAuthorizedClientRegistrationId();

//            중간 결과 출력
            log.info("socialMemberProvider : {}", socialMemberProvider);
            log.info("attributes : {}", attributes);

            String memberEmail = null;
            String socialMemberProviderId = null;
            String memberName = null;

//            각 소셜 인증 종류 별로 필요한 기능 수행
//            변수에 값 할당
            if("google".equals(socialMemberProvider)){
                memberEmail = (String)attributes.get("email");
                socialMemberProviderId = (String)attributes.get("sub");
                memberName = (String)attributes.get("name");
            }else if("kakao".equals(socialMemberProvider)){
                socialMemberProviderId = String.valueOf(attributes.get("id"));
                Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>)kakaoAccount.get("profile");

                memberEmail = (String)kakaoAccount.get("email");
                memberName = (String)profile.get("nickname");

            }else if("naver".equals(socialMemberProvider)){
                Map<String, Object> naverResponse = (Map<String, Object>) attributes.get("response");
                socialMemberProviderId = (String)naverResponse.get("id");
                memberEmail = (String)naverResponse.get("email");
                memberName = (String)naverResponse.get("name");
            }

//            소셜 회원가입 및 로그인을 위한 요청을 할 때 담아서 보낼 값들을 DTO 에 넣기
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setMemberEmail(memberEmail);
            memberDTO.setSocialMemberProviderId(socialMemberProviderId);
            memberDTO.setMemberName(memberName);
            memberDTO.setSocialMemberProvider(socialMemberProvider);

//            로그인 및 JWT 토큰 키 발급
            JwtTokenDTO jwtTokenDTO = authService.socialLogin(memberDTO);
            log.info("jwtTokenDTO : {}", jwtTokenDTO);

//            accessToken, refresh 토큰을 쿠키에 삽입
            ResponseCookie accessTokenCookie = ResponseCookie
                    .from("accessToken", jwtTokenDTO.getAccessToken())
                    .httpOnly(true) // XSS 공격 차단
                    .sameSite("Lax") // CSRF 공격 차단
                    .path("/")
                    .secure(false) // 개발 환경 false, 배포 환경 true (http <-> https)
                    .maxAge(60 * 60 * 24) // 쿠키 만료 기간
                    .build();

            ResponseCookie refreshTokenCookie = ResponseCookie
                    .from("refreshToken", jwtTokenDTO.getRefreshToken())
                    .httpOnly(true) // XSS 공격 차단
                    .sameSite("Lax") // CSRF 공격 차단
                    .path("/")
                    .secure(false) // 개발 환경 false, 배포 환경 true (http <-> https)
                    .maxAge(60 * 60 * 24 * 30) // 쿠키 만료 기간
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

            String redirectUrl = "http://localhost:3000";
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        }
    }
}

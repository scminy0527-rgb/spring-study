package com.app.oauth.config;

import com.app.oauth.domain.dto.response.ApiResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint  implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 모든 private 페이지에서 토큰 인증 예외처리 담당
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Api Response DTO 설계 후 반환
        log.info("필요한 페이지 에서 토큰이 없음");
        ApiResponseDTO apiResponseDTO = ApiResponseDTO.of("토큰 없음 또는 인증 실패");
        String json = objectMapper.writeValueAsString(apiResponseDTO);
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}

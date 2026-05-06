package com.app.oauth.api;

import com.app.oauth.service.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class PublicFileAPI {

    private final StorageService storageService;

    // GET /api/files/2026/04/30/uuid_cat.jpg
    // → S3에서 파일을 받아 클라이언트로 직접 스트리밍
    // → 주소창 URL이 변하지 않으므로 S3 URL 완전 미노출
    @GetMapping("/**")
    public void stream(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String relativePath = request.getRequestURI().replaceFirst("/api/files/", "");
        storageService.stream(relativePath, response);
    }
}

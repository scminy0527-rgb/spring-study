package com.app.oauth.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StorageService {
    // 단일 파일 업로드 → 상대 경로(키) 반환
    String upload(MultipartFile file) throws IOException;

    // 다중 파일 업로드 → 상대 경로(키) 목록 반환
    List<String> uploadMultiple(List<MultipartFile> files) throws IOException;

    // 상대 경로 → 실제 스토리지 풀 URL (백엔드 내부에서만 사용)
    String resolveUrl(String relativePath);

    // S3에서 파일을 받아 HttpServletResponse로 직접 스트리밍 (URL 미노출)
    void stream(String relativePath, HttpServletResponse response) throws IOException;
}

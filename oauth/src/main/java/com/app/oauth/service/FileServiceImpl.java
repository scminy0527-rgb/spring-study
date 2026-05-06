package com.app.oauth.service;

import com.app.oauth.domain.dto.response.ApiResponseDTO;
import com.app.oauth.exception.FileException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class})
public class FileServiceImpl implements FileService {

    private final StorageService storageService;

//    파일 1개를 AWS 클라우드에 올리기
//    파일을 해당 매서드에 전달 하면 클라우드에 올리고
//    반환값으로 올라간 파일의 상대주소를 반환
    @Override
    public ApiResponseDTO uploadFile(MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 상대 경로(키)만 반환 — S3 URL은 StorageService 내부에서만 관리
            String relativePath = storageService.upload(file);
            response.put("updatedUrl", relativePath);
        } catch (IOException e) {
            throw new FileException("파일 업로드 실패", HttpStatus.BAD_REQUEST);
        }
        return ApiResponseDTO.of(true, "업로드 성공", response);
    }

//    파일 여러개를 AWS 클라우드에 올리기
//    파일을 해당 매서드에 전달 하면 클라우드에 올리고
//    반환값으로 올라간 파일'들'의 상대주소'들'을 반환
    @Override
    public ApiResponseDTO uploadFiles(List<MultipartFile> files) throws IOException {
        Map<String, Object> response = new HashMap<>();
        try {
            List<String> relativePaths = storageService.uploadMultiple(files);
            response.put("updatedUrls", relativePaths);
        } catch (IOException e) {
            throw new FileException("파일 업로드 실패", HttpStatus.BAD_REQUEST);
        }
        return ApiResponseDTO.of(true, "업로드 성공", response);
    }
}

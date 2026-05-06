package com.app.oauth.api;

import com.app.oauth.domain.dto.response.ApiResponseDTO;
import com.app.oauth.service.FileService;
import com.app.oauth.util.AwsS3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/private/api/file")
@RequiredArgsConstructor
public class FileAPI {

    private final FileService fileService;

//    메서드 들은 사진 파일은 한개 혹은 여러개 올린 다음에 반환값으로 올라간 파일들의
//    상대주소를 반환

    // 사진과 같은 파일 형태는 json 파싱 대상이아님 (바이너리)
    // 따라서 RequestParam 이용
    // 1개 업로드
    @PostMapping("/upload-file")
    public ResponseEntity<ApiResponseDTO> upload(
            @RequestParam("uploadFile") MultipartFile uploadFile,
            Authentication authentication
    ) throws IOException {
//        log.info("uploadedUrl : {}", uploadedUrl);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fileService.uploadFile(uploadFile));
    }

    // 여러 개 업로드
    @PostMapping("/upload-files")
    public ResponseEntity<ApiResponseDTO> uploads(
            @RequestParam("uploadFiles") List<MultipartFile> uploadFiles,
            Authentication authentication
    ) throws IOException {
        log.info("uploadFiles : {}", uploadFiles);
//        List<String> uploadedUrls = awsS3Util.uploadFiles(uploadFiles);
//        for(String uploadedUrl : uploadedUrls){
//            log.info("uploadedUrl : {}", uploadedUrl);
//        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fileService.uploadFiles(uploadFiles));
    }
}




















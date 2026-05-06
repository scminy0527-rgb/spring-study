package com.app.oauth.service;

import com.app.oauth.domain.dto.response.ApiResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
//    파일 1개 업로드
    public ApiResponseDTO uploadFile(MultipartFile file) throws IOException;

//    파일 여러개 업로드
    public ApiResponseDTO uploadFiles(List<MultipartFile> files) throws IOException;

    //
}

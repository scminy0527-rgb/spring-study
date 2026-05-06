package com.app.oauth.service;

import com.app.oauth.util.AwsS3Util;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3StorageServiceImpl implements StorageService {

    private final AwsS3Util awsS3Util;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Override
    public String upload(MultipartFile file) throws IOException {
        return awsS3Util.uploadFile(file);
    }

    @Override
    public List<String> uploadMultiple(List<MultipartFile> files) throws IOException {
        return awsS3Util.uploadFiles(files);
    }

    // S3 URL은 이 메서드만 알고 있음 — FileServiceImpl, Controller는 몰라도 됨
    @Override
    public String resolveUrl(String relativePath) {
        return "https://" + bucket.trim() + ".s3." + region.trim() + ".amazonaws.com/" + relativePath;
    }

    // S3에서 파일을 꺼내 클라이언트로 직접 스트리밍 — 주소창에 S3 URL 미노출
    @Override
    public void stream(String relativePath, HttpServletResponse response) throws IOException {
        try (ResponseInputStream<GetObjectResponse> s3Object = awsS3Util.downloadFile(relativePath)) {
            GetObjectResponse metadata = s3Object.response();

            response.setContentType(metadata.contentType());
            if (metadata.contentLength() != null) {
                response.setContentLengthLong(metadata.contentLength());
            }

            s3Object.transferTo(response.getOutputStream());
        }
    }
}

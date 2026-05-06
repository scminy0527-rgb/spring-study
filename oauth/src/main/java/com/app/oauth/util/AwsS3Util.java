package com.app.oauth.util;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsS3Util {
    private final S3Client s3Client;
    private final String buckket = "testapp-scminy0527";

    public String getPath(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd/"));
    }

    // 1개 업로드
    public String uploadFile(MultipartFile uploadFile) throws IOException{
        String dataPath = getPath();
        String url = null;

        if(uploadFile.getContentType().startsWith("image")){
            String uuid = UUID.randomUUID().toString();
            String originalFileName =  uploadFile.getOriginalFilename();
            String uploadFileName = uuid + "_" + originalFileName;
            String key = dataPath + uploadFileName;

            // 파일을 byte[]로 한 번만 읽어둠 (InputStream은 소비되면 재사용 불가)
            byte[] fileBytes = uploadFile.getBytes();

            // 이미지 추가
            s3Client.putObject(
                    PutObjectRequest
                            .builder()
                            .bucket(buckket)
                            .key(key)
                            .contentType(uploadFile.getContentType())
                            .build(),
                    RequestBody.fromBytes(fileBytes)
            );

            // 이미지 경로를 url에 추가 (상대 키만 반환, S3 URL은 StorageService에서 조합)
            url = key;

            // 썸네일 제작
            String thumbName = "t_" + uploadFileName;
            String thumbKey = dataPath + thumbName;

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Thumbnailator.createThumbnail(
                    new java.io.ByteArrayInputStream(fileBytes), // 저장해둔 byte[]로 새 스트림 생성
                    byteArrayOutputStream,
                    100,
                    100);

            byte[] thumbBytes = byteArrayOutputStream.toByteArray();

            // 썸네일 등록
            s3Client.putObject(
                    PutObjectRequest
                            .builder()
                            .bucket(buckket)
                            .key(thumbKey)
                            .contentType(uploadFile.getContentType())
                            .build(),
                    RequestBody.fromBytes(thumbBytes)
            );

            byteArrayOutputStream.close();
        }

        // 경로(키) 반환
        return url;
    }

    // 여러개 업로드 (+썸네일)
    public List<String> uploadFiles(List<MultipartFile> uploadFiles) throws IOException {
        String dataPath = getPath();
        List<String> urls = new ArrayList<>();

        for(MultipartFile uploadFile : uploadFiles){

            // 이미지라면
            if(uploadFile.getContentType().startsWith("image")){
                String uuid = UUID.randomUUID().toString();
                String originalFileName =  uploadFile.getOriginalFilename();
                String uploadFileName = uuid + "_" + originalFileName;
                String key = dataPath + uploadFileName;
                String url = key;

                // 파일을 byte[]로 한 번만 읽어둠 (InputStream은 소비되면 재사용 불가)
                byte[] fileBytes = uploadFile.getBytes();

                // 이미지 추가
                s3Client.putObject(
                        PutObjectRequest
                                .builder()
                                .bucket(buckket)
                                .key(key)
                                .contentType(uploadFile.getContentType())
                                .build(),
                        RequestBody.fromBytes(fileBytes)
                );

                // 이미지 경로를 urls에 추가
                urls.add(url);

                // 썸네일 제작
                String thumbName = "t_" + uploadFileName;
                String thumbKey = dataPath + thumbName;

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Thumbnailator.createThumbnail(
                        new java.io.ByteArrayInputStream(fileBytes), // 저장해둔 byte[]로 새 스트림 생성
                        byteArrayOutputStream,
                        100,
                        100);

                byte[] thumbBytes = byteArrayOutputStream.toByteArray();

                // 썸네일 등록
                s3Client.putObject(
                        PutObjectRequest
                                .builder()
                                .bucket(buckket)
                                .key(thumbKey)
                                .contentType(uploadFile.getContentType())
                                .build(),
                        RequestBody.fromBytes(thumbBytes)
                );

                byteArrayOutputStream.close();
            }
        }
        // 경로들 반환
        return urls;
    }

    // S3에서 파일 스트림을 꺼내 반환 — 호출자가 close() 해야 함
    // 이미지에 대한 타입을 그대로 들고 와야 함 (바이트로 바꾸면 안됨)
    public ResponseInputStream<GetObjectResponse> downloadFile(String key) {
        return s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(buckket)
                        .key(key)
                        .build()
        );
    }
}













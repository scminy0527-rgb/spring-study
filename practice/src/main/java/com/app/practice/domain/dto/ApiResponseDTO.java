package com.app.practice.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Schema(description = "api 요청에 대한 응답값")
public class ApiResponseDTO<T> {
    @Schema(description = "응답 메시지", example = "ㅇㅇ 성공", required = true)
    private String message;
    @Schema(description = "응답 결과 데이터", example = "DTO")
    private T data;

    public static <T> ApiResponseDTO<T> of(String message) {
        return new ApiResponseDTO<>(message, null);
    }

    public static <T> ApiResponseDTO<T> of(String message, T data) {
        return new ApiResponseDTO<>(message, data);
    }
}

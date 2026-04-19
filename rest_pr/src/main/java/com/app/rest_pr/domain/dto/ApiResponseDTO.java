package com.app.rest_pr.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "요청에 대한 응답 전달하는 DTO")
public class ApiResponseDTO<T> {
    @Schema(description = "메세지", required = true)
    private String message;
    @Schema(description = "결과 데이터")
    private T data;

    public static<T> ApiResponseDTO<T> of(String message, T data) {
        return new ApiResponseDTO<>(message, data);
    }

    public static<T> ApiResponseDTO<T> of(String message) {
        return new ApiResponseDTO<>(message, null);
    }
}

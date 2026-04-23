package com.app.oauth.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ApiResponseDTO<T> {
    private String message;
    private T data;

    public ApiResponseDTO(String message) {
        this.message = message;
    }

    public static <T>ApiResponseDTO<T> of(String message){
        return new ApiResponseDTO<>(message);
    }

    public static <T>ApiResponseDTO<T> of(String message, T data){
        return new ApiResponseDTO<>(message, data);
    }
}

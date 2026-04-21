package com.app.restful.controller;

import com.app.restful.domain.dto.ApiResponseDTO;
import com.app.restful.exceptions.MemberException;
import com.app.restful.exceptions.PostException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(PostException.class)
    public ResponseEntity<ApiResponseDTO> handleException(PostException e) {
        return ResponseEntity.status(e.getStatus())
                .body(ApiResponseDTO.of(e.getMessage()));
    }

//    분기처리는 어떻게 해야 할까 (멤버의 로그인 경우)
    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ApiResponseDTO> handleException(MemberException e) {
        return ResponseEntity.status(e.getStatus())
                .body(ApiResponseDTO.of(e.getMessage()));
    }
}

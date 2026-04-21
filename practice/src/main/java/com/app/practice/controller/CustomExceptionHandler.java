package com.app.practice.controller;

import com.app.practice.domain.dto.ApiResponseDTO;
import com.app.practice.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

//    유저 에러 핸들러
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiResponseDTO> handleException(UserException e){
        return ResponseEntity
                .status(e.getStatus())
                .body(ApiResponseDTO.of(e.getMessage()));
    }
}

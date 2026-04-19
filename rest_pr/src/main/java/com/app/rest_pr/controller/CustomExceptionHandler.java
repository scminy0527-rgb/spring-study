package com.app.rest_pr.controller;

import com.app.rest_pr.domain.dto.ApiResponseDTO;
import com.app.rest_pr.exceptions.MemberException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO> handleException(MemberException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ApiResponseDTO.of(e.getMessage()));
    }
}

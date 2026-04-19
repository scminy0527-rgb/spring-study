package com.app.rest_pr.exceptions;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MemberException extends RuntimeException {
    private String message;
    private HttpStatus status;

    public MemberException() {;}
    public MemberException(String message,  HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }
}

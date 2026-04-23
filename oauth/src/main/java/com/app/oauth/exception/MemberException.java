package com.app.oauth.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class MemberException extends RuntimeException {

    private HttpStatus status;

    public MemberException(String message) {
        super(message);
    }

    public MemberException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}

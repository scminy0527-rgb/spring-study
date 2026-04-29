package com.app.oauth.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class JwtTokenException extends RuntimeException {

    private HttpStatus status;

    public JwtTokenException(String message) {
        super(message);
    }

    public JwtTokenException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}

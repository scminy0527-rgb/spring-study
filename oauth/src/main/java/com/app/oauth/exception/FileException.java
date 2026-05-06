package com.app.oauth.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class FileException extends RuntimeException {

    private HttpStatus status;

    public FileException(String message) {
        super(message);
    }
    public FileException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}

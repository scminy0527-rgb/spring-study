package com.app.restful.exceptions;

import org.springframework.http.HttpStatus;

public class MemberLoginException extends MemberException {
    public MemberLoginException() {;}
    public MemberLoginException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

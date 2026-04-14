package com.app.restful.exceptions;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException() {;}
    public DuplicateEmailException(String message) {
        super("이메일 중복 에러 발생: " + message);
    }
}

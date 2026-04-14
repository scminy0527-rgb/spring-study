package com.app.restful.exceptions;

public class MemberException extends RuntimeException {
    public MemberException() {;}
    public MemberException(String message) {
        super(message);
    }
}

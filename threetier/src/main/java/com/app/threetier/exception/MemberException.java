package com.app.threetier.exception;

public class MemberException extends RuntimeException {
    public MemberException(){;}
    public MemberException(String message) {
        super(message);
    }
}

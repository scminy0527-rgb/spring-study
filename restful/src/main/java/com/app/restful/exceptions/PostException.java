package com.app.restful.exceptions;

public class PostException extends RuntimeException {
    public PostException(){;}
    public PostException(String message) {
        super(message);
    }
}

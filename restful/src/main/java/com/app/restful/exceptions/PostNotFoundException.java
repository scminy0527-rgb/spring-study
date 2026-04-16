package com.app.restful.exceptions;

import org.springframework.http.HttpStatus;

public class PostNotFoundException extends PostException{
    public PostNotFoundException(){;}
    public PostNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

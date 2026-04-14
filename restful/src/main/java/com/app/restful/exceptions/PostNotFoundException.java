package com.app.restful.exceptions;

public class PostNotFoundException extends PostException{
    public PostNotFoundException(){;}
    public PostNotFoundException(String message) {
        super(message);
    }
}

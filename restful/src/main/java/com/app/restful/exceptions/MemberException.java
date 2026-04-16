package com.app.restful.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class MemberException extends RuntimeException {

//    에러 핸들링의 분기 처리를 위한 목적으로 상태코드 추가
    private HttpStatus status;

    public MemberException() {;}
    public MemberException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}

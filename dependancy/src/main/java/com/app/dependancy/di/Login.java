package com.app.dependancy.di;

import lombok.Data;
import org.springframework.stereotype.Component;

//만들었으면 spring 에 보고하기
@Component
@Data
public class Login {
    private final Member member;
}

package com.app.dependancy.di;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

// 1. 만들었으면 spring 에 보고하기
// 2. 의존성 파악 : 멤버는 로그인 없어도 다른 기능 가능하지만, 로그인은 맴버 없다면 빈 깡통
@Component
@Data
public class Member {
//    private final Login login;
    private String name;
}

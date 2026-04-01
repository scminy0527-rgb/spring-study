package com.app.dependancy.di;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Data
//초기화 생성자가 없으면 생성자 주입을 받을 수 없다
public class Food {
    private final Knife knife;
}

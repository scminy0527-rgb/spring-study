package com.app.dependancy.di;


import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Data
@RequiredArgsConstructor
//@Data: getter, setter, toString, hashcode
//@NoArgsConstructor 기본 생성자
//@AllArgsConstructor 초기화 생성자
//@Getter
//@Setter
//@ToString
//@EqualsAndHashCode
public class Coding {

//    필드 주입을 하면 안되는 이유
//    1. 값이 중간에 바뀔 수 있다. 불변상태를 만들 수 없다.
//    final로 해결 X(불가능)
//    2. 순환참조 발생 여부를 알 수 없다.
//    서버 -> 코딩 -> 컴퓨터 -> 코딩 -> 컴퓨터

//    final 은 변수가 만들어 지는 시점 부터 제한 걸림
//    시점 문제 해결 하려면 생성자 이용

//     생성자 주입으로 불변성을 유지하자
    final private Computer computer;

    private String type;
    private String content;
}

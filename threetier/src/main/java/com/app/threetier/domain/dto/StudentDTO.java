package com.app.threetier.domain.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class StudentDTO {
    private Long id;
    private String studentName;
    private int StudentKor;
    private int StudentEng;
    private int StudentMath;

    //    아래는 좀 더 추가적인 값 인 DTO
    private int total;
    private double average;
}

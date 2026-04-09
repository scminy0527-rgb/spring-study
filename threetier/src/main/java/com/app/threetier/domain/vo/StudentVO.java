package com.app.threetier.domain.vo;

import lombok.Data;
import org.springframework.stereotype.Component;
//CREATE OR REPLACE VIEW VIEW_STUDENT AS (
//        SELECT
//                ID, STUDENT_NAME, STUDENT_KOR, STUDENT_ENG, STUDENT_MATH,
//        STUDENT_KOR + STUDENT_ENG + STUDENT_MATH AS TOTAL,
//        ROUND((STUDENT_KOR + STUDENT_ENG + STUDENT_MATH) / 3, 3) AS AVERAGE
//FROM TBL_STUDENT
//);

//CREATE TABLE TBL_STUDENT(
//        ID NUMBER CONSTRAINT PK_STUDENT PRIMARY KEY,
//        STUDENT_NAME VARCHAR2(255) NOT NULL,
//STUDENT_KOR NUMBER DEFAULT 0,
//STUDENT_ENG NUMBER DEFAULT 0,
//STUDENT_MATH NUMBER DEFAULT 0
//        );

@Component
@Data
public class StudentVO {
    private Long id;
    private String studentName;
    private int StudentKor;
    private int StudentEng;
    private int StudentMath;
    private int total;
    private double average;
}

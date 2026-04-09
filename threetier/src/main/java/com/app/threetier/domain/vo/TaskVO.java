package com.app.threetier.domain.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class TaskVO {
    private int kor;
    private int eng;
    private int math;

//    총점 및 평균점수 getter 추가 정의
    public int getTotalScore() {
        return kor + eng + math;
    }
    public int getAverageScore() {
        return this.getTotalScore() / 3;
    }
}

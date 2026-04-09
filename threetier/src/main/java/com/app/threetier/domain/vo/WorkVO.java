package com.app.threetier.domain.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@Data
public class WorkVO {
    private Long id;
    private String workName;
    private String workStart;
    private String workEnd;
    private String workDate;
}

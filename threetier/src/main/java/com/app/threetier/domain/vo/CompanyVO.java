package com.app.threetier.domain.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class CompanyVO {
    private Long id;
    private String companyName;
    private String companyWorkStart;
    private String companyWorkEnd;
}

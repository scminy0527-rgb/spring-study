package com.app.restful.domain.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class MemberUpdateRequestDTO {
    private Long id;
    private String memberPassword;
    private String memberName;
}

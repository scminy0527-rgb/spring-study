package com.app.restful.domain.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class MemberLoginRequestDTO {
    private String memberEmail;
    private String memberPassword;
}

package com.app.mybatis.domain.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class MemberVO {
    private Long id;
    private String memberName;
    private String memberEmail;
    private String memberPassword;
}

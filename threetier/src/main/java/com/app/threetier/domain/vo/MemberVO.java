package com.app.threetier.domain.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

//json 으로 직렬화 하기 위한 거 필요
@Component
@Data
public class MemberVO implements Serializable {
    private Long id;
    private String memberEmail;
    private String memberPassword;
    private String memberName;
}

package com.app.restful.domain.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class PetTourDTO {
//    이건 게터 세터를 이용해서 넣기에 카멜 표기법으로 해도 무방함
    private String contentId;
    private String areaCode;
    private String firstImage;
    private String firstImage2;
    private String tel;
    private String title;
    private String zipcode;
}

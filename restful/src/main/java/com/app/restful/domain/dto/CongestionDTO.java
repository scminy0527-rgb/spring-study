package com.app.restful.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class CongestionDTO {
//    키 이름이랑 변수 이름 같으면 json property 생략 가능
    private Integer page;
    private Integer perPage;
    private Integer totalCount;
    private Integer currentCount;
    private Integer matchCount;

    @JsonProperty("data")
    private List<CongestionDataDTO> congestionDTOList;
}

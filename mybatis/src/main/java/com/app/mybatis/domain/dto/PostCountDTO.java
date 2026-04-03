package com.app.mybatis.domain.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class PostCountDTO {
    private Long totalPostCount;
    private Long totalPageCount;
}

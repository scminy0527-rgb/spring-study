package com.app.threetier.domain.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ProductVO {
    private Long id;
    private String productName;
    private Integer productPrice;
    private Integer productStock;
    private String productBrand;
}

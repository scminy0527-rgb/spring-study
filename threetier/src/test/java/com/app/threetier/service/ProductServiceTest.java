package com.app.threetier.service;

import com.app.threetier.domain.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class ProductServiceTest {
    @Autowired
    private ProductServiceImpl productService;

    @Test
    public void addProductTest() {
        ProductVO productVO = new ProductVO();
        productVO.setProductName("갤럭시 S26");
        productVO.setProductPrice(1190000);
        productVO.setProductStock(10);
        productVO.setProductBrand("Samsung");

        productService.addProduct(productVO);
    }

    @Test
    public void getProductTest() {
        log.info("상품 조회: {}", productService.getProduct(100L));
    }
}

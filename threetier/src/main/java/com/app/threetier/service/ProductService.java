package com.app.threetier.service;

import com.app.threetier.domain.vo.ProductVO;

public interface ProductService {
    public void addProduct(ProductVO productVO);
    public ProductVO getProduct(Long id);
}

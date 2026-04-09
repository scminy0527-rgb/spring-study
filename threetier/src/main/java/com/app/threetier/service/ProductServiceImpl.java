package com.app.threetier.service;

import com.app.threetier.domain.vo.ProductVO;
import com.app.threetier.exception.ProductException;
import com.app.threetier.repository.ProductDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = {Exception.class})
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductDAO productDAO;

    @Override
    public void addProduct(ProductVO productVO) {
        productDAO.write(productVO);
    }

    @Override
    public ProductVO getProduct(Long id) {
        return productDAO.findById(id).orElseThrow(() ->
                (new ProductException("상품을 찾을 수 없습니다.")));
    }
}

package com.app.threetier.repository;

import com.app.threetier.domain.vo.ProductVO;
import com.app.threetier.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductDAO {
    private final ProductMapper productMapper;

//    상품 작성
    public void write(ProductVO productVO){
        productMapper.insert(productVO);
    }

//    상품 조회 (단건)
    public Optional<ProductVO> findById(Long id){
        return productMapper.select(id);
    }
}

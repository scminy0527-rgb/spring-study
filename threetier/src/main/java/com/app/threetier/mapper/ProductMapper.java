package com.app.threetier.mapper;

import com.app.threetier.domain.vo.ProductVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface ProductMapper {
    public void insert(ProductVO productVO);
    public Optional<ProductVO> select(Long id);
}

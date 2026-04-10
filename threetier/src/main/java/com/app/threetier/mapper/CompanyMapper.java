package com.app.threetier.mapper;

import com.app.threetier.domain.vo.CompanyVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompanyMapper {
//    출근 기록을 기록하는 함수
    void insert(CompanyVO companyVO);
}

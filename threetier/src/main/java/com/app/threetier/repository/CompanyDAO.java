package com.app.threetier.repository;

import com.app.threetier.domain.vo.CompanyVO;
import com.app.threetier.mapper.CompanyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CompanyDAO {
    private final CompanyMapper companyMapper;

//    출근 내역 기록
    public void register(CompanyVO companyVO){
        companyMapper.insert(companyVO);
    }
}

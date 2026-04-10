package com.app.threetier.service;

import com.app.threetier.domain.vo.CompanyVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class CompanyServiceTest {
    @Autowired
    private CompanyServiceImpl companyService;

    @Test
    public void registerCommuteTest() {
        CompanyVO companyVO = new CompanyVO();
        companyVO.setCompanyName("코리아 아이티");
//        companyVO.setCompanyWorkStart("2026-04-10 11:17:00");
        companyVO.setCompanyWorkEnd("2026-04-10 18:00:00");

        companyService.registerCommute(companyVO);
    }
}

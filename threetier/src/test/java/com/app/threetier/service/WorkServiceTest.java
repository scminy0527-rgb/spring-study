package com.app.threetier.service;

import com.app.threetier.domain.vo.WorkVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class WorkServiceTest {
    @Autowired
    private WorkServiceImpl workService;

    @Test
    public void workStartTest() {
        WorkVO workVO = new WorkVO();
        workVO.setWorkName("노규호");
        workVO.setWorkStart("2026-04-09");

        if(workService.workStart(workVO)){
            log.info("출근 처리 완료");
        } else {
            log.info("중복 출근 오류: 이미 출근하셨습니다.");
        }
    }

    @Test
    public void workEndTest() {
        WorkVO workVO = new WorkVO();
        workVO.setWorkName("노규호");
        workVO.setWorkStart("2026-04-09");

        if(workService.workStart(workVO)){
            log.info("퇴근 처리 완료");
        } else {
            log.info("중복 퇴근 오류: 이미 퇴근하셨습니다.");
        }
    }
}

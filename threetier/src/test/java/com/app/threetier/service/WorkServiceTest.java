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

//    출근 여부 확인 해주는 함수 정상 작동 확인
    @Test
    public void isComeWorkTest() {
        WorkVO workVO = new WorkVO();
        workVO.setWorkName("이규학");
        workVO.setWorkStart("2026-04-05");

        if(workService.isComeWork(workVO)){
            log.info("출근 한 상태");
        } else {
            log.info("출근 안함");
        }
    }

    @Test
    public void isEarlyExitTest() {
        WorkVO workVO = new WorkVO();
        workVO.setWorkName("이규학");
        workVO.setWorkEnd("2026-04-09");

        if(workService.isEarlyExit(workVO)){
            log.info("이미 퇴실 처리 하셨습니다.");
        } else {
            log.info("노 퇴실");
        }
    }

    @Test
    public void endWorkTest() {
        WorkVO workVO = new WorkVO();
        workVO.setWorkStart("2026-04-09");
        workVO.setWorkEnd("2026-04-09");
        workVO.setWorkName("박하영");

        if(workService.endWork(workVO)){
            log.info("정상 퇴실 처리 완료되었습니다.");
        } else {
            log.info("퇴실 불가, 관리자 문의 필요");
        }
    }

//    조퇴 여부 확인
    @Test
    public void isJotaiTest() {
        WorkVO workVO = new WorkVO();
        workVO.setWorkStart("2026-04-09");
        workVO.setWorkEnd("2026-04-09");
        workVO.setWorkName("박하영");
        if(workService.isJotai(workVO)){
            log.info("조퇴 함");
        } else {
            log.info("정상 퇴근임");
        }
    }

}

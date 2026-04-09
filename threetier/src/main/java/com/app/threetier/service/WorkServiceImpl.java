package com.app.threetier.service;

import com.app.threetier.domain.vo.WorkVO;
import com.app.threetier.repository.WorkDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class WorkServiceImpl implements WorkService {

    private final WorkDAO workDAO;

    @Override
    public boolean workStart(WorkVO workVO) {
        boolean result = false;
//        먼저 금일 출근 여부 먼저 확인
        if(workDAO.existByWorkNameAndWorkStart(workVO) == 0){
            workDAO.write(workVO);
            result = true;
        }

        return result;
    }

    @Override
    public boolean isLate(Long id) {
        boolean isInTime = workDAO.checkLateStatusById(id) == 0;
        return isInTime;
    }

//    퇴근하는 거 정의
    @Override
    public boolean endWork(WorkVO workVO) {
        boolean result = true;
        workDAO.updateWorkEnd(workVO);
        return result;
    }

    @Override
    public boolean isEarlyExit() {
        LocalDateTime now = LocalDateTime.now();

        return false;
    }
}

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
        if(!this.isComeWork(workVO)){
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
        boolean result = false;
//        출근을 했으면서 아직 퇴실을 처리 안했어야지 가능
        if(this.isComeWork(workVO) && !this.isEarlyExit(workVO)){
            workDAO.updateWorkEnd(workVO);
            result = true;
        }
        return result;
    }


//    특정 인원이 이미 퇴실을 찍었는 지 확인을 하는 로직
//    퇴실 처리를 했다면 true
//    아직 안했으면 false
    @Override
    public boolean isEarlyExit(WorkVO workVO) {
        return (workDAO.findIsExit(workVO) != 0);
    }

//    해당 인원의 출근 여부를 확인 하는거도 정의
//    출근 하면 true, 안했으면 false
    @Override
    public boolean isComeWork(WorkVO workVO) {
        return workDAO.existByWorkNameAndWorkStart(workVO) != 0;
    }

//    조퇴 했따면 true
    @Override
    public boolean isJotai(WorkVO workVO) {
        return workDAO.findIsEarlyExit(workVO) == 1;
    }
}

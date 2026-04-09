package com.app.threetier.repository;

import com.app.threetier.domain.vo.WorkVO;
import com.app.threetier.mapper.WorkMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WorkDAO {
    private final WorkMapper workMapper;
    private final WorkVO workVO;

    //    출근 도장 찍기
    public void write(WorkVO workVO){
        workMapper.insert(workVO);
    }

//    금일 기 출근 여부 확인
    public int existByWorkNameAndWorkStart(WorkVO workVO){
        return workMapper.existByWorkNameAndWorkStart(workVO);
    }

//    지각 여부 확인
    public int checkLateStatusById(Long id){
        return workMapper.selectLateStatusById(id);
    }


//    퇴근 및 조퇴에 관현 한 것
    public void updateWorkEnd(WorkVO workVO){
        workMapper.updateWorkEnd(workVO);
    }

    public int findIsExit(WorkVO workVO){
        return workMapper.existByWorkNameAndWorkEnd(workVO);
    }

    public int findIsEarlyExit(WorkVO workVO){
        return workMapper.selectExitEarlyStatusByWorkStartAndWorkNameAndWorkEnd(workVO);
    }
}

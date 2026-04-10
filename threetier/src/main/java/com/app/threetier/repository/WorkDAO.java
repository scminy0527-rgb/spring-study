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
//    해당 기능을 통해서 출근 여부도 확인 가능
    public int existByWorkNameAndWorkStart(WorkVO workVO){
        return workMapper.existByWorkNameAndWorkStart(workVO);
    }

//    지각 여부 확인
    public int checkLateStatusById(Long id){
        return workMapper.selectLateStatusById(id);
    }


//    퇴근 및 조퇴에 관현 한 것
//    퇴근 처리를 하는 기능
    public void updateWorkEnd(WorkVO workVO){
        workMapper.updateWorkEnd(workVO);
    }

//    퇴근 여부 확인
//    이미 퇴근 처리를 한 여부에 대한 확인
//    퇴근 처리를 했다면 1 이상의 숫자 나옴
    public int findIsExit(WorkVO workVO){
        return workMapper.existByWorkNameAndWorkEnd(workVO);
    }

//    조퇴 "여부" 를 반환하는 것
//    만약 조퇴를 했다면 1
//    정상 퇴근이라면 0 반환
    public int findIsEarlyExit(WorkVO workVO){
        return workMapper.selectExitEarlyStatusByWorkStartAndWorkNameAndWorkEnd(workVO);
    }
}

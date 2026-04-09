package com.app.threetier.mapper;

import com.app.threetier.domain.vo.WorkVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkMapper {
    public void insert(WorkVO workVO);
    public int existByWorkNameAndWorkStart(WorkVO workVO);
    public int selectLateStatusById(Long id);

//    퇴근 및 조퇴에 관한거
    public void updateWorkEnd(WorkVO workVO);
    public int existByWorkNameAndWorkEnd(WorkVO workVO);
    public int selectExitEarlyStatusByWorkStartAndWorkNameAndWorkEnd(WorkVO workVO);
}

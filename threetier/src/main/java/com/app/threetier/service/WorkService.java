package com.app.threetier.service;

import com.app.threetier.domain.vo.WorkVO;

public interface WorkService {
    public boolean workStart(WorkVO workVO);
    public boolean isLate(Long id);

    public boolean endWork(WorkVO workVO);
    public boolean isEarlyExit(WorkVO workVO);

    public boolean isComeWork(WorkVO workVO);
    public boolean isJotai(WorkVO workVO);
}

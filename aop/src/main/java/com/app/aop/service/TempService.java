package com.app.aop.service;

import com.app.aop.aspect.annotation.LogStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.lang.Integer.*;

@Service
@Slf4j
public class TempService {

    // @AfterReturning 검증: 정상 반환 케이스
    @LogStatus
    public int doAdd(String str1, String str2) {
        log.info("[핵심로직] doAdd 실행");
        return parseInt(str1) + parseInt(str2);
    }

    // @AfterReturning 검증: 다른 정상 반환 케이스
    @LogStatus
    public int doSubtract(String str1, String str2) {
        log.info("[핵심로직] doSubtract 실행");
        return parseInt(str1) - parseInt(str2);
    }

    // @AfterThrowing 검증: NumberFormatException 발생 케이스
    @LogStatus
    public int doAddWithInvalidInput(String str1, String str2) {
        log.info("[핵심로직] doAddWithInvalidInput 실행");
        return parseInt(str1) + parseInt(str2); // 숫자가 아닌 문자열이면 예외 발생
    }
}

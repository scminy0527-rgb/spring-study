package com.app.aop.aspect.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Aspect
@Configuration
@Slf4j
public class LogAspect {

    // 포인트컷 재사용: @LogStatus 어노테이션이 붙은 모든 메서드
    @Pointcut("@annotation(com.app.aop.aspect.annotation.LogStatus)")
    public void logStatusPointcut() {}

    // @Before: 메서드 실행 직전 — 인자값 확인, 사전 검증 등에 활용
    @Before("logStatusPointcut()")
    public void before(JoinPoint joinPoint) {
        log.info("▶ [@Before] 메서드 시작");
        log.info("   메서드명 : {}", joinPoint.getSignature().getName());
        log.info("   전달 인자 : {}", Arrays.toString(joinPoint.getArgs()));
        log.info("   대상 객체 : {}", joinPoint.getTarget().getClass().getSimpleName());
    }

    // @After: 메서드 종료 후 항상 실행 — 정상/예외 무관 (finally 역할)
    @After("logStatusPointcut()")
    public void after(JoinPoint joinPoint) {
        log.info("■ [@After] 메서드 종료 (항상 실행) — 메서드명: {}", joinPoint.getSignature().getName());
    }

    // @AfterReturning: 정상 반환 후 실행 — returning으로 반환값에 접근 가능
//    이게 @After 보다 먼저 이전에 수행됨
    @AfterReturning(pointcut = "logStatusPointcut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        log.info("✔ [@AfterReturning] 정상 반환 — 메서드명: {}, 반환값: {}", joinPoint.getSignature().getName(), result);
    }

    // @AfterThrowing: 예외 발생 후 실행 — throwing으로 예외 객체에 접근 가능
//    이건 예외가 발생 하는데 어떤걸 무조건 던질거냐?
//    예외가 발생한 다음에 예가 어떻게 처리 할거냐? (try catch 등등 어떻게 할 지 정할 수 있음)
    @AfterThrowing(pointcut = "logStatusPointcut()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex) {
        log.error("✘ [@AfterThrowing] 예외 발생 — 메서드명: {}, 예외: {}", joinPoint.getSignature().getName(), ex.getMessage());
    }

    // @Around: 실행 전후 전체를 감싸는 가장 강력한 Advice
    //          proceed()를 직접 호출해야 실제 메서드가 실행됨
//    기존 전체를 감싸는 거
//    메인 관점을 다시 재정의 해야 하는 경우에 사용하는 어노테이션
    @Around("logStatusPointcut()")
    public Integer around(ProceedingJoinPoint joinPoint) throws Throwable {
//        프로시드 포인터 컷: 만나는 부분을 자르기
//        integer 0 :

//        조인 포인트로 들고 오는 재정의 값은파라미터가 잘못 되었을 수 있기에 반드시 예외처리를 해야한다.
        long start = System.currentTimeMillis();
        log.info("⟳ [@Around] 실행 전 — 메서드명: {}", joinPoint.getSignature().getName());
        try {
//            이렇게 숫자 형 관련 해서 오류를 다 빼놓으면
//            다른 부분에서도 어노테이션 하나 붙어 놓으면 전부 에러를 잡을 수 있다. (예가 다 처리 가능)
            Integer result = (Integer) joinPoint.proceed(); // 실제 메서드 호출
            long elapsed = System.currentTimeMillis() - start;
            log.info("⟳ [@Around] 실행 후 — 소요시간: {}ms, 반환값: {}", elapsed, result);

//            메인에서 반환이 있다면 여기서 반환 해줘야 하고, 반환이 없다면 여기서도 반환 없어야 함
            return result;
        } catch (Throwable ex) {
            log.error("⟳ [@Around] 예외 감지 — 예외 타입: {}", ex.getClass().getSimpleName());
            throw ex; // 예외를 다시 던져야 @AfterThrowing 등 후속 Advice가 정상 동작함
        }
    }
}

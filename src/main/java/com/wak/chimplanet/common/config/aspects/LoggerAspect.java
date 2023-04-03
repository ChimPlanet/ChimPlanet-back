package com.wak.chimplanet.common.config.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


/**
 * 메소드 시작 및 종료시점에 로그
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LoggerAspect {

    @Before("execution(* com.wak.chimplanet..*(..))")
    public void logMethodAccessBefore(JoinPoint joinPoint) {
        String _class = joinPoint.getTarget().getClass().getSimpleName();
        String _method = joinPoint.getSignature().getName();

        log.debug("START : {}", _class + _method);
    }

    @AfterReturning("execution(* com.wak.chimplanet..*(..))")
    public void logMethodAccessAfter(JoinPoint joinPoint) {
        String _class = joinPoint.getTarget().getClass().getSimpleName();
        String _method = joinPoint.getSignature().getName();

        log.debug("END : {}", _class + _method);
    }
}

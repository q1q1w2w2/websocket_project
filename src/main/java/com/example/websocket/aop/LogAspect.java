package com.example.websocket.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.example.websocket.aop.LogExecutionTime)")
    private void timer(){}

    @Around("timer()")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch sw = new StopWatch();
        // 해당 클래스 처리 전 시간
        sw.start();
        // 해당 클래스의 로직 실행
        Object result = joinPoint.proceed();
        // 해당 클래스 처리 후 시간
        sw.stop();

        long executionTime = sw.getTotalTimeMillis();

        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        String task = className + ". " + methodName;


        log.info("[ExecutionTime] {} --> {}ms", task, executionTime);
        return result;
    }
}

package com.msaProject.core.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Before: 대상 “메서드”가 실행되기 전에 Advice를 실행합니다.
     *
     * @param joinPoint
     */
    @Before("execution(* com.msaProject.board.controller.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
    	//logger.info("Before: " + joinPoint.getSignature().getName());
    }

    /**
     * After : 대상 “메서드”가 실행된 후에 Advice를 실행합니다.
     *
     * @param joinPoint
     */
    @After("execution(* com.msaProject.board.controller.*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
    	//logger.info("After: " + joinPoint.getSignature().getName());
    }

    /**
     * AfterReturning: 대상 “메서드”가 정상적으로 실행되고 반환된 후에 Advice를 실행합니다.
     *
     * @param joinPoint
     * @param result
     */
    @AfterReturning(pointcut = "execution(* com.msaProject.board.controller.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
    	logger.info("AfterReturning: " + joinPoint.getSignature().getName() + " result: " + result);
    }

    /**
     * AfterThrowing: 대상 “메서드에서 예외가 발생”했을 때 Advice를 실행합니다.
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "execution(* com.msaProject.board.controller.*.*(..))", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
    	logger.info("예외 발생: " + joinPoint.getSignature().getName() + " exception: " + e.getMessage());
    }

    /**
     * Around : 대상 “메서드” 실행 전, 후 또는 예외 발생 시에 Advice를 실행합니다.
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.msaProject.board.controller.*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    	logger.info("실행 메소드: " + joinPoint.getSignature().getName());
        Object result = joinPoint.proceed();
        logger.info("실행 후 메소드: " + joinPoint.getSignature().getName());
        return result;
    }
    
    /**
     * Around advice to calculate the execution time of methods in the specified packages.
     *
     * @param joinPoint
     * @return Object (result of the method execution)
     * @throws Throwable
     */
    @Around("execution(* com.msaProject.board.controller..*(..)) || execution(* com.msaProject.board.service..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object proceed = joinPoint.proceed(); // Execute the method

        long executionTime = System.currentTimeMillis() - startTime;

        logger.info("{} executed in {} ms", joinPoint.getSignature(), executionTime);

        return proceed;
    }

}

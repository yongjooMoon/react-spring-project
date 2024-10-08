package com.msaProjectMenu01.core.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Aspect
@Component
public class TransactionAspect {

    private static final Logger logger = LoggerFactory.getLogger(TransactionAspect.class);

    private final TransactionTemplate transactionTemplate;

    public TransactionAspect(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    @Around("execution(* com.msaProjectMenu01.menu.service..*(..))")  // 서비스 계층의 모든 메서드에 적용
    public Object applyTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        return transactionTemplate.execute(status -> {
            try {
                logger.info("Transaction started for method: {}", joinPoint.getSignature());
                Object result = joinPoint.proceed(); // 메서드 실행
                logger.info("Transaction successful for method: {}", joinPoint.getSignature());
                return result;
            } catch (Throwable throwable) {
                logger.error("Transaction failed for method: {}", joinPoint.getSignature(), throwable);
                status.setRollbackOnly();
                throw new RuntimeException(throwable);
            }
        });
    }
    
    @Before("execution(* com.msaProjectMenu01.menu.service..*(..))")
    public void beforeTransaction() {
        logger.info("Transaction started");
    }
}

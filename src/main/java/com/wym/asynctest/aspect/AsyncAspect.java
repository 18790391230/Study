package com.wym.asynctest.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

/**
 * @author v_ymmwu
 */
@Component
@Aspect
@Slf4j
public class AsyncAspect {


    @Pointcut(value = "execution(* com.wym.asynctest.service.*.*(..))")
    private void foo1() {

    }

    @Around(value = "foo1()")
    public Object around(ProceedingJoinPoint joinPoint) {
        try {
            if (joinPoint instanceof MethodInvocationProceedingJoinPoint) {
                log.info("{}开始执行", joinPoint.getSignature().toShortString());
            }else {
                log.info("{}开始执行", joinPoint);
            }

            Object proceed = joinPoint.proceed();

            log.info("{}执行结束", joinPoint.getSignature().toString());
            return proceed;
        } catch (Throwable throwable) {
            return null;
        }
    }

}

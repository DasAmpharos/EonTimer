package com.github.dylmeadows.eontimer.aspect.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("@annotation(annotation)")
    void beforeLogExecutionTime(JoinPoint joinPoint, LogExecutionTime annotation) {
        System.out.println(joinPoint);
    }

    @After("@annotation(annotation)")
    void afterLogExecutionTime(JoinPoint joinPoint, LogExecutionTime annotation) {
        System.out.println(joinPoint);
    }
}

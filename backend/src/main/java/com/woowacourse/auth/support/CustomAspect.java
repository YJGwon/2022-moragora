package com.woowacourse.auth.support;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CustomAspect {

    @Before(value = "@annotation(Master) && args(masterId,..)")
    public void logArgs(JoinPoint joinPoint, Long masterId) {
        System.out.println("=========id: " + masterId);
    }
}

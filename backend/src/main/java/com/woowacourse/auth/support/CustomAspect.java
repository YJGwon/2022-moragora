package com.woowacourse.auth.support;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CustomAspect {
    // @Master annotation 가지고, masterId를 첫번째 param으로 가지는 method에 대해 동작
    @Before(value = "@annotation(Master) && args(..,loginId)")
    public void logArgs(Long loginId) {
        System.out.println("=========id: " + loginId);
    }

    @After("within(com.woowacourse.moragora.controller.ControllerAdvice) && args(exception)")
    public void logExceptions(Exception exception) {
        exception.
        System.out.println("=========exception: " + exception.getMessage());
    }
}

package com.d204.rumeet.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Slf4j
@Component
public class LogAop {
    @Pointcut(value = "execution(* com.d204.rumeet.*.controller.*.*(..))")
    private void logDisplaying() {
    }

    @Around(value = "logDisplaying()")
    public void aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String requestUri = request.getRequestURI();

        log.debug("{} uri : {}", request.getMethod(), requestUri);
        int cnt = 0;
        for (Object param : joinPoint.getArgs()) {
            log.debug("param{} : {}", cnt++, param);
        }

        Object result = joinPoint.proceed();
        log.debug("return : {}", result);
    }
}

package com.prgrms.p2p.domain.common.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class LogAspect {

  @Around("within(com.prgrms.p2p.domain.common.exception..*)")
  public Object logException(ProceedingJoinPoint joinPoint) throws Throwable {
    if (joinPoint.getArgs()[0] instanceof RuntimeException) {
      RuntimeException e = (RuntimeException) joinPoint.getArgs()[0];
      log.warn("{} : {}", joinPoint.getSignature().toShortString(), e.getMessage());
    }
    return joinPoint.proceed();
  }

  @Around("@annotation(LogExecutionTime)")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    Object proceed = joinPoint.proceed();
    stopWatch.stop();
    log.info("{} : {}", joinPoint.getSignature().toShortString(), stopWatch);
    return proceed;
  }
}

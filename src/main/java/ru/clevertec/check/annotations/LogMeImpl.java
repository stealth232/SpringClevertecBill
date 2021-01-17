package ru.clevertec.check.annotations;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class LogMeImpl {
    static Logger logger = LogManager.getLogger();

    @Around("@annotation(ru.clevertec.check.annotations.LogMe)")
    public void logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Object o;
        long start = System.currentTimeMillis();
        o = joinPoint.proceed();
       logger.info("!!!!!! Logger time = "+ (System.currentTimeMillis()-start));
    }
}


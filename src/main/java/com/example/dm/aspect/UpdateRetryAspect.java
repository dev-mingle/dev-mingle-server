package com.example.dm.aspect;

import com.example.dm.annotation.UpdateRetry;
import com.example.dm.exception.UpdateFailureException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Order(1)
@Aspect
@Component
public class UpdateRetryAspect {

    @Around("@annotation(updateRetry)")
    public Object doUpdateRetry(ProceedingJoinPoint joinPoint, UpdateRetry updateRetry) throws Throwable {
        int retryCount = updateRetry.value();
        ObjectOptimisticLockingFailureException exception = null;

        for (int cnt = 0; cnt < retryCount; cnt++) {
            try {
                return joinPoint.proceed();
            } catch (ObjectOptimisticLockingFailureException ex) {
                exception = ex;
                // 5번 재시도 이후 50millis 딜레이
                if (cnt > 4) Thread.sleep(50);
            }
        }

        throw new UpdateFailureException("잠시후 다시 시도해 주세요.", exception);
    }
}

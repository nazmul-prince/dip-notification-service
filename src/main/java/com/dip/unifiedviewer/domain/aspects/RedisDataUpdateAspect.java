package com.dip.unifiedviewer.domain.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dip.unifiedviewer.constansts.RedisConstants;

import java.lang.reflect.Method;

@Aspect
@Component
public class RedisDataUpdateAspect {

    private static final Logger logger = LoggerFactory.getLogger(RedisDataUpdateAspect.class);
    private final RMapCache<String, String> rMapCache;

    public RedisDataUpdateAspect(RedissonClient redissonClient) {
        this.rMapCache = redissonClient.getMapCache(RedisConstants.REDIS_MAP_KEY);
    }

    @Around("@annotation(RedisLock)")
    public Object lockAroundUpdateStatus(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedisLock redisLockAnnotation = method.getAnnotation(RedisLock.class);
        String lockKey = redisLockAnnotation.keyPrefix() + joinPoint.getArgs()[0];
        logger.info("Acquiring redis lock at " + method.getName() + " with key " + lockKey);
        RLock redisLock = rMapCache.getLock(lockKey);
        redisLock.lock();

        Object returnValue = null;
        try{
            returnValue = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally{
            logger.info("Releasing redis lock at " + method.getName() + " with key " + lockKey);
            redisLock.unlock();
        }
        return returnValue;
    }
}

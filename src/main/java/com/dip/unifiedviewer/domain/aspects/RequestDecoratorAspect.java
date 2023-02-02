package com.dip.unifiedviewer.domain.aspects;

import com.dip.unifiedviewer.constansts.ApiExposureConstants;
import com.dip.unifiedviewer.domain.model.requests.BaseRequestBodyModel;

import org.apache.commons.codec.binary.Hex;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Aspect
@Component
public class RequestDecoratorAspect {

  private static final Logger logger = LoggerFactory.getLogger(RequestDecoratorAspect.class);

//  @Before("execution(* com.dip.publicapiexposure.controller.DataController.*(..))")
//  public void decorateRequest(JoinPoint joinPoint) {
//    Object object = joinPoint.getArgs()[0];
//    BaseRequestBodyModel requestBodyModel = (BaseRequestBodyModel) object;
//
//    if (Objects.equals(requestBodyModel.getRequestId(), null)) {
//      requestBodyModel.setRequestId(UUID.randomUUID().toString());
//    }
//
//    MDC.put(ApiExposureConstants.LOG_KEY_REQUEST_ID, requestBodyModel.getRequestId());
//  }

  @Before("execution(* com.dip.unifiedviewer.storage.redis.service.RedisMessageSubscriber.onMessage(..))")
  public void decorateRequestRedisListener(JoinPoint joinPoint) {
    Object object = joinPoint.getArgs()[1];
    byte[] bytes = (byte[]) object;

    String channel = new String(bytes, StandardCharsets.UTF_8);

    MDC.put(ApiExposureConstants.LOG_KEY_REQUEST_ID, channel);
  }
}

package com.dip.unifiedviewer.domain.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** When using this annotation, the method's first parameter must be the request id. */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {

  /** provide the redis key prefix here. */
  String keyPrefix();
}

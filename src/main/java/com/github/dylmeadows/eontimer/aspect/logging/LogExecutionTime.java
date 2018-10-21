package com.github.dylmeadows.eontimer.aspect.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    LoggingMethod loggingMethod() default LoggingMethod.DEBUG;
}

package com.github.dylmeadows.eontimer.aspect.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogParameters {

    LoggingMethod loggingMethod() default LoggingMethod.DEBUG;
}

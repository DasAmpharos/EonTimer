package com.github.dylmeadows.eontimer.util.aspect;

import org.slf4j.event.Level;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Metrics {

    MetricType[] value();

    Level logLevel() default Level.DEBUG;
}

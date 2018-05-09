package com.github.dylmeadows.eontimer;

import com.github.dylmeadows.EonTimer;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class EonTimerLogger {

    private final Logger log;

    private static EonTimerLogger logger;

    private static final String[] LOG_SYSTEM_PROPERTIES = {
            "os.name",
            "os.version",
            "os.arch",
            "java.version",
            "java.vendor",
            "sun.arch.data.model"
    };

    private EonTimerLogger() {
        log = Logger.getLogger(EonTimerLogger.class);
    }

    public static EonTimerLogger getLogger() {
        if (logger == null)
            logger = new EonTimerLogger();
        return logger;
    }

    public void initialize() {
        log.info(EonTimer.NAME + " " + EonTimer.VERSION);
        for (String property : LOG_SYSTEM_PROPERTIES) {
            log.info(property + " == " + System.getProperty(property));
        }
    }

    public void trace(String message) {
        log.trace(message);
    }

    public void trace(Throwable t) {
        log.trace(t.getMessage(), t);
    }

    public void trace(String message, Throwable t) {
        log.trace(message, t);
    }

    public void trace(Object o) {
        log.trace(o);
    }

    public void trace(Object o, Throwable t) {
        log.trace(o, t);
    }

    public void debug(String message) {
        log.debug(message);
    }

    public void debug(Throwable t) {
        log.debug(t.getMessage(), t);
    }

    public void debug(String message, Throwable t) {
        log.debug(message, t);
    }

    public void debug(Object o) {
        log.debug(o);
    }

    public void debug(Object o, Throwable t) {
        log.debug(o, t);
    }

    public void info(String message) {
        log.info(message);
    }

    public void info(Throwable t) {
        log.info(t.getMessage(), t);
    }

    public void info(String message, Throwable t) {
        log.info(message, t);
    }

    public void info(Object o) {
        log.info(o);
    }

    public void info(Object o, Throwable t) {
        log.info(o, t);
    }

    public void warn(String message) {
        log.warn(message);
    }

    public void warn(Throwable t) {
        log.warn(t.getMessage(), t);
    }

    public void warn(String message, Throwable t) {
        log.warn(message, t);
    }

    public void warn(Object o) {
        log.warn(o);
    }

    public void warn(Object o, Throwable t) {
        log.warn(o, t);
    }

    public void error(String message) {
        log.error(message);
    }

    public void error(Throwable t) {
        log.error(t.getMessage(), t);
    }

    public void error(String message, Throwable t) {
        log.error(message, t);
    }

    public void error(Object o) {
        log.error(o);
    }

    public void error(Object o, Throwable t) {
        log.error(o, t);
    }

    public void fatal(String message) {
        log.fatal(message);
    }

    public void fatal(Throwable t) {
        log.fatal(t.getMessage(), t);
    }

    public void fatal(String message, Throwable t) {
        log.fatal(message, t);
    }

    public void fatal(Object o) {
        log.fatal(o);
    }

    public void fatal(Object o, Throwable t) {
        log.fatal(o, t);
    }

    public void log(Priority priority, String message) {
        log.log(priority, message);
    }

    public void log(Priority priority, Throwable t) {
        log.log(priority, t.getMessage(), t);
    }

    public void log(Priority priority, String message, Throwable t) {
        log.log(priority, message, t);
    }

    public void log(Priority priority, Object o) {
        log.log(priority, o);
    }

    public void log(Priority priority, Object o, Throwable t) {
        log.log(priority, o, t);
    }
}

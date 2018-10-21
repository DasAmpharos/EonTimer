package com.github.dylmeadows.eontimer.aspect.tracing;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkState;

@Getter
@Setter(AccessLevel.PACKAGE)
class Benchmark {

    private long start = UNINITIALIZED;
    private long stop = UNINITIALIZED;

    private static final Long UNINITIALIZED = (long) -1;

    public long getTotalTime(TimeUnit timeUnit) {
        return TimeUnit.MILLISECONDS.convert(stop - start, timeUnit);
    }

    boolean isRunning() {
        return hasStarted() && !hasStopped();
    }

    boolean isComplete() {
        return hasStarted() && hasStopped();
    }

    private boolean hasStarted() {
        return start != -1;
    }

    private boolean hasStopped() {
        return stop != -1;
    }

    public void start() {
        checkState(start == UNINITIALIZED, "unable to start a benchmark that has already started");
        this.start = System.currentTimeMillis();
    }

    public void stop() {
        checkState(start != UNINITIALIZED, "unable to stop a benchmark that has not be started");
        checkState(stop == UNINITIALIZED, "unable to stop a benchmark that has already run");
        this.stop = System.currentTimeMillis();
    }
}

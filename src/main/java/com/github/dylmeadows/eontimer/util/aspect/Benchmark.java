package com.github.dylmeadows.eontimer.util.aspect;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

@Getter
@Setter(AccessLevel.PACKAGE)
public class Benchmark {

    private long start = -1;
    private long stop = -1;

    public long getTotalTime(TimeUnit timeUnit) {
        return TimeUnit.MILLISECONDS.convert(stop - start, timeUnit);
    }

    public boolean isRunning() {
        return start != -1 && stop == -1;
    }

    public boolean isComplete() {
        return start != -1 && stop != -1;
    }

    public boolean isStarted() {
        return start != -1;
    }

    public boolean isStopped() {
        return stop != -1;
    }

    public void start() {
        if (start == -1) {
            this.start = System.currentTimeMillis();
        }
    }

    public void stop() {
        if (start != -1 && stop == -1) {
            this.stop = System.currentTimeMillis();
        }
    }
}

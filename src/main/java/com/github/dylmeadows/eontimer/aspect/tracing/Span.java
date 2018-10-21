package com.github.dylmeadows.eontimer.aspect.tracing;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

@Getter
@Setter
public class Span {

    private String name;

    private Benchmark benchmark;

    @Setter(AccessLevel.PACKAGE)
    private Span parent;

    private static final ThreadLocal<Span> currentSpan = new ThreadLocal<>();

    public static Span getCurrentSpan() {
        return currentSpan.get();
    }

    static void setCurrentSpan(Span span) {
        currentSpan.set(span);
    }

    public void start() {
        benchmark = Optional.ofNullable(benchmark).orElseGet(Benchmark::new);
        checkState(!benchmark.isRunning(), "unable to start a span that is already running");
        checkState(!benchmark.isComplete(), "unable to start a span that is already complete");
        benchmark.start();
    }

    public void stop() {
        checkNotNull(benchmark, "benchmark cannot be null");
        checkState(benchmark.isRunning(), "unable stop a span that has not been started");
        checkState(!benchmark.isComplete(), "unable to stop a span that is already complete");
        benchmark.stop();
    }
}

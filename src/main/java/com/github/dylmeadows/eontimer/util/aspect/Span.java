package com.github.dylmeadows.eontimer.util.aspect;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.github.dylmeadows.eontimer.util.Functional.negate;

@Getter
@Setter
public class Span {

    private String name;

    private Benchmark benchmark;

    @Setter(AccessLevel.PACKAGE)
    private Span parent;

    private static final Map<Thread, Span> spans = new HashMap<>();

    public static Span getCurrentSpan() {
        return spans.get(Thread.currentThread());
    }

    static void setCurrentSpan(Span span) {
        spans.put(Thread.currentThread(), span);
    }

    public void start() {
        benchmark = Optional.ofNullable(benchmark)
                .orElse(new Benchmark());
        if (benchmark.isRunning() || benchmark.isComplete()) {
            throw new IllegalStateException("unable to start a span that is already running or complete");
        }
        benchmark.start();
    }

    public void stop() {
        Optional.ofNullable(benchmark)
                .filter(negate(Benchmark::isRunning))
                .orElseThrow(() -> new IllegalStateException("unable to stop a span that has not been started"));
        benchmark.stop();
    }
}

package io.github.dylmeadows.eontimer.core;

@FunctionalInterface
@SuppressWarnings({"unused", "WeakerAccess"})
public interface TimerOnStop {
    void onTimerStop(TimerLifeCycleEvent stopEvent);
}

package io.github.dylmeadows.eontimer.core;

@FunctionalInterface
@SuppressWarnings({"unused", "WeakerAccess"})
public interface TimerOnStart {
    void onTimerStart(TimerLifeCycleEvent startEvent);
}

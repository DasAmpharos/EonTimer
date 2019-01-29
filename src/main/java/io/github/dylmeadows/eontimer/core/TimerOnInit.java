package io.github.dylmeadows.eontimer.core;

@FunctionalInterface
@SuppressWarnings({"unused", "WeakerAccess"})
public interface TimerOnInit {
    void onTimerInit(TimerLifeCycleEvent initEvent);
}

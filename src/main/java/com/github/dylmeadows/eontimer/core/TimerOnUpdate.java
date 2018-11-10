package com.github.dylmeadows.eontimer.core;

@FunctionalInterface
@SuppressWarnings({"unused", "WeakerAccess"})
public interface TimerOnUpdate {
    void onTimerUpdate(TimerLifeCycleEvent updateEvent);
}

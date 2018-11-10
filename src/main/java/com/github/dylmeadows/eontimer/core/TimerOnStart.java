package com.github.dylmeadows.eontimer.core;

import com.github.dylmeadows.eontimer.model.Timer;

@FunctionalInterface
@SuppressWarnings({"unused", "WeakerAccess"})
public interface TimerOnStart {
    void onTimerStart(TimerLifeCycleEvent startEvent);
}

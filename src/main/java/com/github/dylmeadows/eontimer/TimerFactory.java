package com.github.dylmeadows.eontimer;

import com.github.dylmeadows.eontimer.timers.Timer;

public interface TimerFactory {
    Timer createTimer();
}

package com.github.dylmeadows.eontimer.timers;

public interface TimerCalibrator<T extends Timer> {
    int calibrate(T timer, int result);
}

package com.github.dylmeadows.eontimer.model.timers;

public interface TimerCalibrator<T extends Timer> {
    int calibrate(T timer, int result);
}

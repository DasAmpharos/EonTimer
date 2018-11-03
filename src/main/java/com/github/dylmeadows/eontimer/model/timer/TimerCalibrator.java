package com.github.dylmeadows.eontimer.model.timer;

public interface TimerCalibrator<T extends Timer> {
    int calibrate(T timer, int result);
}

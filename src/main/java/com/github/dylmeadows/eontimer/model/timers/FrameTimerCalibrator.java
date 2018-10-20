package com.github.dylmeadows.eontimer.model.timers;

import com.github.dylmeadows.eontimer.util.Calibrations;
import com.github.dylmeadows.eontimer.model.Console;

public class FrameTimerCalibrator implements TimerCalibrator<FrameTimer> {
    @Override
    public int calibrate(FrameTimer timer, int result) {
        // TODO: get console from settings singleton
        Console placeholder = Console._3DS;
        return Calibrations.convertToMillis(timer.getTargetFrame() - result, placeholder);
    }
}

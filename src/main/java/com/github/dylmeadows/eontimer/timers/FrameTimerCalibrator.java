package com.github.dylmeadows.eontimer.timers;

import com.github.dylmeadows.eontimer.CalibrationHelper;
import com.github.dylmeadows.eontimer.reference.Console;

public class FrameTimerCalibrator implements TimerCalibrator<FrameTimer> {
    @Override
    public int calibrate(FrameTimer timer, int result) {
        // TODO: get console from settings singleton
        Console placeholder = Console._3DS;
        return CalibrationHelper.convertToMillis(timer.getTargetFrame() - result, placeholder);
    }
}

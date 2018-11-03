package com.github.dylmeadows.eontimer.model.timer;

import com.github.dylmeadows.eontimer.util.CalibrationUtils;
import com.github.dylmeadows.eontimer.model.Console;

public class FrameTimerCalibrator implements TimerCalibrator<FrameTimer> {
    @Override
    public int calibrate(FrameTimer timer, int result) {
        // TODO: get console from settings singleton
        Console placeholder = Console._3DS;
        return CalibrationUtils.convertToMillis(timer.getTargetFrame() - result, placeholder);
    }
}

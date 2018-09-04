package com.github.dylmeadows.eontimer.timers;

import com.github.dylmeadows.eontimer.CalibrationHelper;
import com.github.dylmeadows.eontimer.reference.Console;

public class FrameTimerCalculator implements TimerCalculator<FrameTimer> {
    @Override
    public int[] getStages(FrameTimer timer) {
        // TODO: get console from settings singleton
        Console placeholder = Console._3DS;
        return new int[]{
                timer.getPreTimer(),
                CalibrationHelper.convertToMillis(timer.getTargetFrame(), placeholder) + timer.getCalibration()
        };
    }

    @Override
    public int getMinutesBeforeTarget(FrameTimer timer) {
        return 0;
    }
}

package com.github.dylmeadows.eontimer.model.timers;

import com.github.dylmeadows.eontimer.util.Calibrations;
import com.github.dylmeadows.eontimer.model.Console;

public class FrameTimerCalculator implements TimerCalculator<FrameTimer> {
    @Override
    public int[] getStages(FrameTimer timer) {
        // TODO: get console from settings singleton
        Console placeholder = Console._3DS;
        return new int[]{
                timer.getPreTimer(),
                Calibrations.convertToMillis(timer.getTargetFrame(), placeholder) + timer.getCalibration()
        };
    }

    @Override
    public int getMinutesBeforeTarget(FrameTimer timer) {
        return 0;
    }
}

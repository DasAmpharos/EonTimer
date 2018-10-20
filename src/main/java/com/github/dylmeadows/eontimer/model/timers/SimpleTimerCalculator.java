package com.github.dylmeadows.eontimer.model.timers;

import java.util.stream.IntStream;

public class SimpleTimerCalculator implements TimerCalculator<SimpleTimer> {
    @Override
    public int[] getStages(SimpleTimer timer) {
        int stage = (timer.getTargetSecond() * 1000) + timer.getCalibration() + 200;
        while (stage < MINIMUM_LENGTH) stage += 60000;
        return new int[]{stage};
    }

    @Override
    public int getMinutesBeforeTarget(SimpleTimer timer) {
        return IntStream.of(getStages(timer)).sum() / 60000;
    }
}

package com.github.dylmeadows.eontimer.model.timer;

public class SimpleTimerCalibrator implements TimerCalibrator<SimpleTimer> {
    @Override
    public int calibrate(SimpleTimer timer, int result) {
        if (result < timer.getTargetSecond()) {
            return ((timer.getTargetSecond() - result) * 1000) - 500;
        } else if (result > timer.getTargetSecond()) {
            return ((timer.getTargetSecond() - result) * 1000) + 500;
        } else {
            return 0;
        }
    }
}

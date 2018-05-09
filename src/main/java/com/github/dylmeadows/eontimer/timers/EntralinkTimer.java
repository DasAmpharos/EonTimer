package com.github.dylmeadows.eontimer.timers;

import com.github.dylmeadows.eontimer.reference.Console;
import com.github.dylmeadows.eontimer.reference.timer.TimerConstants;

import java.time.Duration;

public class EntralinkTimer extends DelayTimer {

    private final int entralinkCalibration;

    public EntralinkTimer(int calibration,
                          int entralinkCalibration,
                          int targetDelay,
                          int targetSecond,
                          Console console,
                          int minimumLength) {
        this(calibration, entralinkCalibration, targetDelay, targetSecond, console, minimumLength, true);
    }

    public EntralinkTimer(int calibration,
                          int entralinkCalibration,
                          int targetDelay,
                          int targetSecond,
                          Console console,
                          int minimumLength,
                          boolean initialize) {
        super(calibration, targetDelay, targetSecond, console, minimumLength, false);
        this.entralinkCalibration = entralinkCalibration;
        if (initialize) initialize();
    }

    @Override
    protected Duration getStage(int stage) {
        switch (stage) {
            case 0:
                return super.getStage(0).plus(Duration.ofMillis(250));
            case 1:
                return super.getStage(1).minus(Duration.ofMillis(getEntralinkCalibration()));
            default:
                return TimerConstants.NULL_TIME_SPAN;
        }
    }

    public int getEntralinkCalibration() {
        return entralinkCalibration;
    }
}

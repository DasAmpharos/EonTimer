package com.github.dylmeadows.eontimer.model.timer;

import com.github.dylmeadows.eontimer.model.Console;

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

        // TODO: refactor
        /*if (initialize) initialize();*/
    }

    @Override
    protected Duration getStage(int stage) {
        switch (stage) {
            case 0:
                return super.getStage(0).plus(Duration.ofMillis(250));
            case 1:
                return super.getStage(1).minus(Duration.ofMillis(getEntralinkCalibration()));
            default:
                // TODO: refactor
                /*return TimerConstants.NULL_TIME_SPAN;*/
                return Duration.ZERO;
        }
    }

    public int getEntralinkCalibration() {
        return entralinkCalibration;
    }
}

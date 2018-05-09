package com.github.dylmeadows.eontimer.timers;

import com.github.dylmeadows.eontimer.CalibrationHelper;
import com.github.dylmeadows.eontimer.reference.Console;
import com.github.dylmeadows.eontimer.reference.timer.TimerConstants;

import java.time.Duration;

public class FrameTimer extends BaseTimer {

    private final int calibration;

    private final int preTimer;

    private final int targetFrame;

    private final Console console;

    public FrameTimer(int calibration, int preTimer, int targetFrame, Console console) {
        this(calibration, preTimer, targetFrame, console, true);
    }

    public FrameTimer(int calibration, int preTimer, int targetFrame, Console console, boolean initialize) {
        this.calibration = calibration;
        this.preTimer = preTimer;
        this.targetFrame = targetFrame;
        this.console = console;

        if (initialize) initialize();
    }

    public static int calibrate(FrameTimer timer, int result) {
        return CalibrationHelper.convertToMillis((timer.getTargetFrame() - result), timer.getConsole());
    }

    @Override
    protected void initialize() {
        int i = 0;
        Duration stage;
        while ((stage = getStage(i)) != TimerConstants.NULL_TIME_SPAN) {
            getStages().add(stage);
            i++;
        }
    }

    @Override
    public int getMinutesBeforeTarget() {
        return 0;
    }

    private Duration getStage(int stage) {
        switch (stage) {
            case 0:
                return Duration.ofMillis(preTimer);
            case 1:
                return Duration.ofMillis(CalibrationHelper.convertToMillis(targetFrame, console) + calibration);
            default:
                return TimerConstants.NULL_TIME_SPAN;
        }
    }

    public int getCalibration() {
        return calibration;
    }

    public int getPreTimer() {
        return preTimer;
    }

    public int getTargetFrame() {
        return targetFrame;
    }

    public Console getConsole() {
        return console;
    }
}

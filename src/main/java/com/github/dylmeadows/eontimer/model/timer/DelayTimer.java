package com.github.dylmeadows.eontimer.model.timer;

import com.github.dylmeadows.eontimer.model.Console;

import java.time.Duration;

public class DelayTimer extends SimpleTimer {

    private final int targetDelay;

    public DelayTimer(int calibration, int targetDelay, int targetSecond, Console console, int minimumLength) {
        this(calibration, targetDelay, targetSecond, console, minimumLength, true);
    }

    public DelayTimer(int calibration, int targetDelay, int targetSecond, Console console, int minimumLength, boolean initialize) {
        // TODO: refactor
        // super(calibration, targetSecond, console, minimumLength, false);
        this.targetDelay = targetDelay;

        // TODO: refactor
        /*if (initialize)
            initialize();*/
    }

    public static int calibrate(DelayTimer timer, int result) {
        // TODO: refactor
        /*result = CalibrationUtils.convertToMillis(result, timer.getConsole());
        int target = CalibrationUtils.convertToMillis(timer.getTargetDelay(), timer.getConsole());

        int offset = result - target;

        if (Math.abs(offset) <= TimerConstants.CLOSE_THRESHOLD)
            offset = (int) (TimerConstants.CLOSE_UPDATE_FACTOR * offset);
        else
            offset *= (int) TimerConstants.UPDATE_FACTOR;

        return offset;*/
        return 0;
    }

    protected Duration getStage(int stage) {
        /*switch (stage) {
            case 0:
                Duration duration = Duration.ofMillis((int) super.getStage(0).toMillis() - CalibrationUtils.convertToMillis(getTargetDelay(), getConsole()));
                while ((int) duration.toMillis() < getMinimumLength())
                    duration = duration.plus(Duration.ofMinutes(1));
                return duration;
            case 1:
                return Duration.ofMillis(CalibrationUtils.convertToMillis(getTargetDelay(), getConsole()) - getCalibration());
            default:
                return TimerConstants.NULL_TIME_SPAN;
        }*/
        return Duration.ZERO;
    }

    public int getTargetDelay() {
        return targetDelay;
    }
}

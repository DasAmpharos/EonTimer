package com.github.dylmeadows.eontimer.timers;

import com.github.dylmeadows.eontimer.reference.Console;
import com.github.dylmeadows.eontimer.reference.timer.TimerConstants;

import java.time.Duration;

public class SimpleTimer extends BaseTimer {

    private final int calibration;

    private final int targetSecond;

    private final Console console;

    private final int minimumLength;

    public SimpleTimer(int calibration, int targetSecond, Console console, int minimumLength) {
        this(calibration, targetSecond, console, minimumLength, true);
    }

    public SimpleTimer(int calibration, int targetSecond, Console console, int minimumLength, boolean initialize) {
        this.calibration = calibration;
        this.targetSecond = targetSecond;
        this.console = console;
        this.minimumLength = minimumLength;

        if (initialize)
            initialize();
    }

    public static int calibrate(SimpleTimer timer, int result) {
        if (result == timer.getTargetSecond())
            return 0;
        else if (result > timer.getTargetSecond())
            return (timer.getTargetSecond() - result) * 1000 + 500;
        else
            return (timer.getTargetSecond() - result) * 1000 - 500;
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
        Duration duration = Duration.ofMillis(0);
        for (Duration stage : getStages())
            duration = duration.plus(stage);
        return (int) duration.toMillis() / 60000;
    }

    protected Duration getStage(int stage) {
        switch (stage) {
            case 0:
                int first = getTargetSecond() * 1000 + getCalibration() + 200;
                while (first < getMinimumLength())
                    first += 60000;
                return Duration.ofMillis(first);
            default:
                return TimerConstants.NULL_TIME_SPAN;
        }
    }

    public int getCalibration() {
        return calibration;
    }

    public int getTargetSecond() {
        return targetSecond;
    }

    public Console getConsole() {
        return console;
    }

    public int getMinimumLength() {
        return minimumLength;
    }
}

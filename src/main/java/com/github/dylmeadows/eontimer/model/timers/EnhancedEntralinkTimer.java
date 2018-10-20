package com.github.dylmeadows.eontimer.model.timers;

import com.github.dylmeadows.eontimer.model.Console;
import com.github.dylmeadows.eontimer.reference.timer.Gen5TimerConstants;

import java.time.Duration;

public class EnhancedEntralinkTimer extends EntralinkTimer implements Gen5TimerConstants {

    private final int targetAdvances;

    private final int npcCount;

    private final int initialAdvances;

    private final int frameCalibration;

    public EnhancedEntralinkTimer(int calibration,
                                  int entralinkCalibration,
                                  int targetDelay,
                                  int targetSecond,
                                  int targetAdvances,
                                  int frameCalibration,
                                  Console console,
                                  int minimumLength) {
        this(calibration, entralinkCalibration, targetDelay, targetSecond, targetAdvances, frameCalibration, console, minimumLength, true);
    }

    public EnhancedEntralinkTimer(int calibration,
                                  int entralinkCalibration,
                                  int targetDelay,
                                  int targetSecond,
                                  int targetAdvances,
                                  int frameCalibration,
                                  Console console,
                                  int minimumLength,
                                  boolean initialize) {
        super(calibration, entralinkCalibration, targetDelay, targetSecond, console, minimumLength, false);
        this.targetAdvances = targetAdvances;
        this.npcCount = 0;
        this.initialAdvances = 0;
        this.frameCalibration = frameCalibration;

        // TODO: refactor
        /*if (initialize) initialize();*/
    }

    public static int calibrate(EnhancedEntralinkTimer timer, int result) {
        // TODO: refactor
        /*double npcRate = 1.0 / Calibrations.convertToMillis(32, timer.getConsole());
        return (int) Math.round((timer.getTargetAdvances() - result) / (ENTRALINK_FRAME_RATE + (timer.getNpcCount() * npcRate))) * 1000;*/
        return 0;
    }

    @Override
    protected Duration getStage(int stage) {
        // TODO: refactor
        /*switch (stage) {
            case 0:
            case 1:
                return super.getStage(stage);
            case 2:
                return calcFrameTime();
            default:
                return TimerConstants.NULL_TIME_SPAN;
        }*/
        return Duration.ZERO;
    }

    public int getMinutesBeforeTarget() {
        // TODO: refactor
        /*Duration duration = Duration.ofMillis(0);

        for (int i = 0; i < 2; i++) {
            duration = duration.plus(getStages().get(i));
        }

        return (int) duration.toMillis() / 60000;*/
        return 0;
    }

    private Duration calcFrameTime() {
        // TODO: refactor
        /*int advances = getTargetAdvances() - getInitialAdvances();
        double npcRate = 1.0 / Calibrations.convertToMillis(32, getConsole());
        int ms = (int) Math.round((advances) / (ENTRALINK_FRAME_RATE + (getNpcCount() * npcRate))) * 1000 + getFrameCalibration();
        return Duration.ofMillis(ms);*/
        return Duration.ZERO;
    }

    public int getTargetAdvances() {
        return targetAdvances;
    }

    public int getNpcCount() {
        return npcCount;
    }

    public int getInitialAdvances() {
        return initialAdvances;
    }

    public int getFrameCalibration() {
        return frameCalibration;
    }
}

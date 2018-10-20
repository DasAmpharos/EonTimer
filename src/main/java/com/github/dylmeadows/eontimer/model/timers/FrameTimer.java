package com.github.dylmeadows.eontimer.model.timers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class FrameTimer implements Timer {

    private final IntegerProperty calibration = new SimpleIntegerProperty();

    private final IntegerProperty preTimer = new SimpleIntegerProperty();

    private final IntegerProperty targetFrame = new SimpleIntegerProperty();

    public int getCalibration() {
        return calibration.get();
    }

    public IntegerProperty calibrationProperty() {
        return calibration;
    }

    public void setCalibration(int calibration) {
        this.calibration.set(calibration);
    }

    public int getPreTimer() {
        return preTimer.get();
    }

    public IntegerProperty preTimerProperty() {
        return preTimer;
    }

    public void setPreTimer(int preTimer) {
        this.preTimer.set(preTimer);
    }

    public int getTargetFrame() {
        return targetFrame.get();
    }

    public IntegerProperty targetFrameProperty() {
        return targetFrame;
    }

    public void setTargetFrame(int targetFrame) {
        this.targetFrame.set(targetFrame);
    }
}

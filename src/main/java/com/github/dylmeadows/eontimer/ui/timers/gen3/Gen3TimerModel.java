package com.github.dylmeadows.eontimer.ui.timers.gen3;

import com.github.dylmeadows.eontimer.model.Gen3TimerMode;
import com.github.dylmeadows.eontimer.reference.timer.Gen3TimerConstants;
import com.github.dylmeadows.eontimer.ui.timers.TimerModel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Gen3TimerModel extends TimerModel implements Gen3TimerConstants {

    private final ObjectProperty<Gen3TimerMode> mode;
    private final IntegerProperty calibration;
    private final IntegerProperty preTimer;
    private final IntegerProperty targetFrame;
    private final transient IntegerProperty frameHit;

    public Gen3TimerModel() {
        mode = new SimpleObjectProperty<>(DEFAULT_MODE);
        calibration = new SimpleIntegerProperty(DEFAULT_CALIBRATION);
        preTimer = new SimpleIntegerProperty(DEFAULT_PRE_TIMER);
        targetFrame = new SimpleIntegerProperty(DEFAULT_TARGET_FRAME);
        frameHit = new SimpleIntegerProperty();
    }

    public Gen3TimerMode getMode() {
        return mode.get();
    }

    public ObjectProperty<Gen3TimerMode> modeProperty() {
        return mode;
    }

    public void setMode(Gen3TimerMode mode) {
        this.mode.set(mode);
    }

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

    public int getFrameHit() {
        return frameHit.get();
    }

    public IntegerProperty frameHitProperty() {
        return frameHit;
    }

    public void setFrameHit(int frameHit) {
        this.frameHit.set(frameHit);
    }
}

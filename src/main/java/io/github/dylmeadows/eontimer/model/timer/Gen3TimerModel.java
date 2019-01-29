package io.github.dylmeadows.eontimer.model.timer;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Gen3TimerModel {

    private final ObjectProperty<Gen3TimerMode> mode = new SimpleObjectProperty<>(Gen3TimerConstants.DEFAULT_MODE);
    private final IntegerProperty calibration = new SimpleIntegerProperty(Gen3TimerConstants.DEFAULT_CALIBRATION);
    private final IntegerProperty preTimer = new SimpleIntegerProperty(Gen3TimerConstants.DEFAULT_PRE_TIMER);
    private final IntegerProperty targetFrame = new SimpleIntegerProperty(Gen3TimerConstants.DEFAULT_TARGET_FRAME);
    private final transient IntegerProperty frameHit = new SimpleIntegerProperty();

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

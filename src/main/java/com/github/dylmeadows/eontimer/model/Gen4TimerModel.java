package com.github.dylmeadows.eontimer.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Gen4TimerModel {

    private final ObjectProperty<Gen4TimerMode> mode = new SimpleObjectProperty<>(Gen4TimerConstants.DEFAULT_MODE);
    private final IntegerProperty calibratedDelay = new SimpleIntegerProperty(Gen4TimerConstants.DEFAULT_CALIBRATED_DELAY);
    private final IntegerProperty calibratedSecond = new SimpleIntegerProperty(Gen4TimerConstants.DEFAULT_CALIBRATED_SECOND);
    private final IntegerProperty targetDelay = new SimpleIntegerProperty(Gen4TimerConstants.DEFAULT_TARGET_DELAY);
    private final IntegerProperty targetSecond = new SimpleIntegerProperty(Gen4TimerConstants.DEFAULT_TARGET_SECOND);
    private final transient IntegerProperty delayHit = new SimpleIntegerProperty();

    public Gen4TimerMode getMode() {
        return mode.get();
    }

    public ObjectProperty<Gen4TimerMode> modeProperty() {
        return mode;
    }

    public void setMode(Gen4TimerMode mode) {
        this.mode.set(mode);
    }

    public int getCalibratedDelay() {
        return calibratedDelay.get();
    }

    public IntegerProperty calibratedDelayProperty() {
        return calibratedDelay;
    }

    public void setCalibratedDelay(int calibratedDelay) {
        this.calibratedDelay.set(calibratedDelay);
    }

    public int getCalibratedSecond() {
        return calibratedSecond.get();
    }

    public IntegerProperty calibratedSecondProperty() {
        return calibratedSecond;
    }

    public void setCalibratedSecond(int calibratedSecond) {
        this.calibratedSecond.set(calibratedSecond);
    }

    public int getTargetDelay() {
        return targetDelay.get();
    }

    public IntegerProperty targetDelayProperty() {
        return targetDelay;
    }

    public void setTargetDelay(int targetDelay) {
        this.targetDelay.set(targetDelay);
    }

    public int getTargetSecond() {
        return targetSecond.get();
    }

    public IntegerProperty targetSecondProperty() {
        return targetSecond;
    }

    public void setTargetSecond(int targetSecond) {
        this.targetSecond.set(targetSecond);
    }

    public int getDelayHit() {
        return delayHit.get();
    }

    public IntegerProperty delayHitProperty() {
        return delayHit;
    }

    public void setDelayHit(int delayHit) {
        this.delayHit.set(delayHit);
    }
}

//    calculatedCalibration = new ReadOnlyIntegerWrapper();
//    calculatedCalibration.bind(Bindings.createIntegerBinding(this::calculateCalibration,
//            calibratedDelay, calibratedSecond, consoleProperty()));
//
//    private int calculateCalibration() {
//        return CalibrationUtils.createCalibration(
//                getCalibratedDelay(),
//                getCalibratedSecond(),
//                getConsole());
//    }

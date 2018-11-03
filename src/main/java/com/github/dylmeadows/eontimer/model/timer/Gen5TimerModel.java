package com.github.dylmeadows.eontimer.model.timer;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import static com.github.dylmeadows.eontimer.model.timer.Gen5TimerConstants.*;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Gen5TimerModel {

    private final ObjectProperty<Gen5TimerMode> mode = new SimpleObjectProperty<>(DEFAULT_MODE);
    private final IntegerProperty calibration = new SimpleIntegerProperty(DEFAULT_CALIBRATION);
    private final IntegerProperty targetDelay = new SimpleIntegerProperty(DEFAULT_TARGET_DELAY);
    private final IntegerProperty targetSecond = new SimpleIntegerProperty(DEFAULT_TARGET_SECOND);
    private final IntegerProperty entralinkCalibration = new SimpleIntegerProperty(DEFAULT_ENTRALINK_CALIBRATION);
    private final IntegerProperty frameCalibration = new SimpleIntegerProperty(DEFAULT_FRAME_CALIBRATION);
    private final IntegerProperty targetAdvances = new SimpleIntegerProperty(DEFAULT_TARGET_ADVANCES);
    private final transient IntegerProperty secondHit = new SimpleIntegerProperty();
    private final transient IntegerProperty delayHit = new SimpleIntegerProperty();
    private final transient IntegerProperty actualAdvances = new SimpleIntegerProperty();

    public Gen5TimerMode getMode() {
        return mode.get();
    }

    public ObjectProperty<Gen5TimerMode> modeProperty() {
        return mode;
    }

    public void setMode(Gen5TimerMode mode) {
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

    public int getEntralinkCalibration() {
        return entralinkCalibration.get();
    }

    public IntegerProperty entralinkCalibrationProperty() {
        return entralinkCalibration;
    }

    public void setEntralinkCalibration(int entralinkCalibration) {
        this.entralinkCalibration.set(entralinkCalibration);
    }

    public int getFrameCalibration() {
        return frameCalibration.get();
    }

    public IntegerProperty frameCalibrationProperty() {
        return frameCalibration;
    }

    public void setFrameCalibration(int frameCalibration) {
        this.frameCalibration.set(frameCalibration);
    }

    public int getTargetAdvances() {
        return targetAdvances.get();
    }

    public IntegerProperty targetAdvancesProperty() {
        return targetAdvances;
    }

    public void setTargetAdvances(int targetAdvances) {
        this.targetAdvances.set(targetAdvances);
    }

    public int getSecondHit() {
        return secondHit.get();
    }

    public IntegerProperty secondHitProperty() {
        return secondHit;
    }

    public void setSecondHit(int secondHit) {
        this.secondHit.set(secondHit);
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

    public int getActualAdvances() {
        return actualAdvances.get();
    }

    public IntegerProperty actualAdvancesProperty() {
        return actualAdvances;
    }

    public void setActualAdvances(int actualAdvances) {
        this.actualAdvances.set(actualAdvances);
    }
}

//    calculatedCalibration = new ReadOnlyIntegerWrapper();
//    calculatedCalibration.bind(Bindings.createIntegerBinding(this::calculateCalibration,
//        precisionCalibrationModeProperty(), calibration, consoleProperty()));
//    calculatedEntralinkCalibration = new ReadOnlyIntegerWrapper();
//    calculatedEntralinkCalibration.bind(Bindings.createIntegerBinding(this::calculateEntralinkCalibration,
//        precisionCalibrationModeProperty(), entralinkCalibration, consoleProperty()));
//
//    private int calculateCalibration() {
//        return isPrecisionCalibrationMode() ? getCalibration() :
//            CalibrationUtils.convertToMillis(getCalibration(), getConsole());
//    }
//
//    private int calculateEntralinkCalibration() {
//        return isPrecisionCalibrationMode() ? getEntralinkCalibration() :
//            CalibrationUtils.convertToMillis(getEntralinkCalibration(), getConsole());
//    }

package com.github.dylmeadows.eontimer.ui.timers.gen4;

import com.github.dylmeadows.eontimer.util.Calibrations;
import com.github.dylmeadows.eontimer.model.Gen4TimerMode;
import com.github.dylmeadows.eontimer.reference.timer.Gen4TimerConstants;
import com.github.dylmeadows.eontimer.ui.timers.TimerModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;

public class Gen4TimerModel extends TimerModel implements Gen4TimerConstants {

    private final ObjectProperty<Gen4TimerMode> mode;
    private final IntegerProperty calibratedDelay;
    private final IntegerProperty calibratedSecond;
    private final IntegerProperty targetDelay;
    private final IntegerProperty targetSecond;

    private final transient IntegerProperty delayHit;

    private final transient ReadOnlyIntegerWrapper calculatedCalibration;

    public Gen4TimerModel() {
        mode = new SimpleObjectProperty<>(DEFAULT_MODE);
        calibratedDelay = new SimpleIntegerProperty(DEFAULT_CALIBRATED_DELAY);
        calibratedSecond = new SimpleIntegerProperty(DEFAULT_CALIBRATED_SECOND);
        targetDelay = new SimpleIntegerProperty(DEFAULT_TARGET_DELAY);
        targetSecond = new SimpleIntegerProperty(DEFAULT_TARGET_SECOND);

        calculatedCalibration = new ReadOnlyIntegerWrapper();
        calculatedCalibration.bind(Bindings.createIntegerBinding(this::calculateCalibration,
                calibratedDelay, calibratedSecond, consoleProperty()));

        delayHit = new SimpleIntegerProperty();
    }

    private int calculateCalibration() {
        return Calibrations.createCalibration(
                getCalibratedDelay(),
                getCalibratedSecond(),
                getConsole());
    }

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

    public int getCalculatedCalibration() {
        return calculatedCalibration.get();
    }

    public ReadOnlyIntegerProperty calculatedCalibrationProperty() {
        return calculatedCalibration.getReadOnlyProperty();
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

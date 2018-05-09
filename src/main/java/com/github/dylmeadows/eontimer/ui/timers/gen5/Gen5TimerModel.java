package com.github.dylmeadows.eontimer.ui.timers.gen5;

import com.github.dylmeadows.eontimer.CalibrationHelper;
import com.github.dylmeadows.eontimer.reference.Gen5TimerMode;
import com.github.dylmeadows.eontimer.reference.timer.Gen5TimerConstants;
import com.github.dylmeadows.eontimer.ui.timers.TimerModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;

public class Gen5TimerModel extends TimerModel implements Gen5TimerConstants {

    private final ObjectProperty<Gen5TimerMode> mode;
    private final IntegerProperty calibration;
    private final IntegerProperty targetDelay;
    private final IntegerProperty targetSecond;
    private final IntegerProperty entralinkCalibration;
    private final IntegerProperty frameCalibration;
    private final IntegerProperty targetAdvances;

    private final transient IntegerProperty secondHit;
    private final transient IntegerProperty delayHit;
    private final transient IntegerProperty actualAdvances;

    private final transient ReadOnlyIntegerWrapper calculatedCalibration;
    private final transient ReadOnlyIntegerWrapper calculatedEntralinkCalibration;

    public Gen5TimerModel() {
        mode = new SimpleObjectProperty<>(DEFAULT_MODE);
        calibration = new SimpleIntegerProperty(DEFAULT_CALIBRATION);
        targetDelay = new SimpleIntegerProperty(DEFAULT_TARGET_DELAY);
        targetSecond = new SimpleIntegerProperty(DEFAULT_TARGET_SECOND);
        entralinkCalibration = new SimpleIntegerProperty(DEFAULT_ENTRALINK_CALIBRATION);
        frameCalibration = new SimpleIntegerProperty(DEFAULT_FRAME_CALIBRATION);
        targetAdvances = new SimpleIntegerProperty(DEFAULT_TARGET_ADVANCES);

        secondHit = new SimpleIntegerProperty();
        delayHit = new SimpleIntegerProperty();
        actualAdvances = new SimpleIntegerProperty();

        calculatedCalibration = new ReadOnlyIntegerWrapper();
        calculatedCalibration.bind(Bindings.createIntegerBinding(this::calculateCalibration,
                precisionCalibrationModeProperty(), calibration, consoleProperty()));
        calculatedEntralinkCalibration = new ReadOnlyIntegerWrapper();
        calculatedEntralinkCalibration.bind(Bindings.createIntegerBinding(this::calculateEntralinkCalibration,
                precisionCalibrationModeProperty(), entralinkCalibration, consoleProperty()));
    }

    private int calculateCalibration() {
        return isPrecisionCalibrationMode() ? getCalibration() :
                CalibrationHelper.convertToMillis(getCalibration(), getConsole());
    }

    private int calculateEntralinkCalibration() {
        return isPrecisionCalibrationMode() ? getEntralinkCalibration() :
                CalibrationHelper.convertToMillis(getEntralinkCalibration(), getConsole());
    }

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

    public int getCalculatedCalibration() {
        return calculatedCalibration.get();
    }

    public ReadOnlyIntegerProperty calculatedCalibrationProperty() {
        return calculatedCalibration.getReadOnlyProperty();
    }

    public int getCalculatedEntralinkCalibration() {
        return calculatedEntralinkCalibration.get();
    }

    public ReadOnlyIntegerProperty calculatedEntralinkCalibrationProperty() {
        return calculatedEntralinkCalibration.getReadOnlyProperty();
    }
}

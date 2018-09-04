package com.github.dylmeadows.eontimer.timers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SimpleTimer implements Timer {

    private final IntegerProperty calibration = new SimpleIntegerProperty();

    private final IntegerProperty targetSecond = new SimpleIntegerProperty();

    public int getCalibration() {
        return calibration.get();
    }

    public IntegerProperty calibrationProperty() {
        return calibration;
    }

    public void setCalibration(int calibration) {
        this.calibration.set(calibration);
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
}

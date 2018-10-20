package com.github.dylmeadows.eontimer.ui.timers;

import com.github.dylmeadows.eontimer.model.Console;
import com.github.dylmeadows.eontimer.reference.settings.TimerSettingsConstants;
import javafx.beans.property.*;

public abstract class TimerModel implements TimerSettingsConstants {

    private final transient ObjectProperty<Console> console;
    private final transient BooleanProperty precisionCalibrationMode;
    private final transient IntegerProperty minimumLength;

    protected TimerModel() {
        console = new SimpleObjectProperty<>(DEFAULT_CONSOLE);
        minimumLength = new SimpleIntegerProperty(DEFAULT_MINIMUM_LENGTH);
        precisionCalibrationMode = new SimpleBooleanProperty(DEFAULT_PRECISION_CALIBRATION_MODE);
    }

    public Console getConsole() {
        return console.get();
    }

    public ObjectProperty<Console> consoleProperty() {
        return console;
    }

    public void setConsole(Console console) {
        this.console.set(console);
    }

    public boolean isPrecisionCalibrationMode() {
        return precisionCalibrationMode.get();
    }

    public BooleanProperty precisionCalibrationModeProperty() {
        return precisionCalibrationMode;
    }

    public void setPrecisionCalibrationMode(boolean precisionCalibrationMode) {
        this.precisionCalibrationMode.set(precisionCalibrationMode);
    }

    public int getMinimumLength() {
        return minimumLength.get();
    }

    public IntegerProperty minimumLengthProperty() {
        return minimumLength;
    }

    public void setMinimumLength(int minimumLength) {
        this.minimumLength.set(minimumLength);
    }
}

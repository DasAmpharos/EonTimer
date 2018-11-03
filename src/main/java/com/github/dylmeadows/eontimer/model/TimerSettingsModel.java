package com.github.dylmeadows.eontimer.model;

import javafx.beans.property.*;

@SuppressWarnings({"unused", "WeakerAccess"})
public class TimerSettingsModel {

    private final ObjectProperty<Console> console = new SimpleObjectProperty<>(TimerSettingsConstants.DEFAULT_CONSOLE);
    private final IntegerProperty refreshInterval = new SimpleIntegerProperty(TimerSettingsConstants.DEFAULT_REFRESH_INTERVAL);
    private final BooleanProperty precisionCalibrationMode = new SimpleBooleanProperty(TimerSettingsConstants.DEFAULT_PRECISION_CALIBRATION_MODE);
    private final transient IntegerProperty minimumLength = new SimpleIntegerProperty(TimerSettingsConstants.DEFAULT_MINIMUM_LENGTH);

    public Console getConsole() {
        return console.get();
    }

    public ObjectProperty<Console> consoleProperty() {
        return console;
    }

    public void setConsole(Console console) {
        this.console.set(console);
    }

    public int getRefreshInterval() {
        return refreshInterval.get();
    }

    public IntegerProperty refreshIntervalProperty() {
        return refreshInterval;
    }

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval.set(refreshInterval);
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

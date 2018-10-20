package com.github.dylmeadows.eontimer.ui.settings.timer;

import com.github.dylmeadows.eontimer.model.Console;
import com.github.dylmeadows.eontimer.reference.settings.TimerSettingsConstants;
import javafx.beans.property.*;

/**
 * Settings related to timer specific configuration. Binds to
 * {@link TimerSettingsView}.
 */
public class TimerSettingsModel implements TimerSettingsConstants {

    /**
     * TODO: come back to this property
     * Binds to {@link TimerSettingsView#consoleField}.
     */
    private final ObjectProperty<Console> console;

    /**
     * See {@link com.github.dylmeadows.eontimer.handlers.TimerMonitor#refreshInterval}.
     * Binds to {@link TimerSettingsView#refreshIntervalField}.
     */
    private final IntegerProperty refreshInterval;

    /**
     * TODO: come back to this property
     * Binds to {@link TimerSettingsView#precisionCalibrationModeField}.
     */
    private final BooleanProperty precisionCalibrationMode;

    /**
     * TODO: come back to this property
     */
    private final transient IntegerProperty minimumLength;

    /**
     * Initializes all properties to their default values.
     */
    public TimerSettingsModel() {
        console = new SimpleObjectProperty<>(DEFAULT_CONSOLE);
        refreshInterval = new SimpleIntegerProperty(DEFAULT_REFRESH_INTERVAL);
        precisionCalibrationMode = new SimpleBooleanProperty(DEFAULT_PRECISION_CALIBRATION_MODE);
        minimumLength = new SimpleIntegerProperty(DEFAULT_MINIMUM_LENGTH);
    }

    /**
     * @return see {@link #console}
     */
    public Console getConsole() {
        return console.get();
    }

    /**
     * @return see {@link #console}
     */
    public ObjectProperty<Console> consoleProperty() {
        return console;
    }

    /**
     * @param console see {@link #console}
     */
    public void setConsole(Console console) {
        this.console.set(console);
    }

    /**
     * @return see {@link #refreshInterval}
     */
    public int getRefreshInterval() {
        return refreshInterval.get();
    }

    /**
     * @return see {@link #refreshInterval}
     */
    public IntegerProperty refreshIntervalProperty() {
        return refreshInterval;
    }

    /**
     * @param refreshInterval see {@link #refreshInterval}
     */
    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval.set(refreshInterval);
    }

    /**
     * @return see {@link #precisionCalibrationMode}
     */
    public boolean isPrecisionCalibrationMode() {
        return precisionCalibrationMode.get();
    }

    /**
     * @return see {@link #precisionCalibrationMode}
     */
    public BooleanProperty precisionCalibrationModeProperty() {
        return precisionCalibrationMode;
    }

    /**
     * @param precisionCalibrationMode see {@link #precisionCalibrationMode}
     */
    public void setPrecisionCalibrationMode(boolean precisionCalibrationMode) {
        this.precisionCalibrationMode.set(precisionCalibrationMode);
    }

    /**
     * @return see {@link #minimumLength}
     */
    public int getMinimumLength() {
        return minimumLength.get();
    }

    /**
     * @return see {@link #minimumLength}
     */
    public IntegerProperty minimumLengthProperty() {
        return minimumLength;
    }

    /**
     * @param minimumLength see {@link #minimumLength}
     */
    public void setMinimumLength(int minimumLength) {
        this.minimumLength.set(minimumLength);
    }
}

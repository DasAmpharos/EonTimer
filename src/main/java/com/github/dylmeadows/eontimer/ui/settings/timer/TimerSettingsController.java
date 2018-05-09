package com.github.dylmeadows.eontimer.ui.settings.timer;

import com.github.dylmeadows.common.javafx.BindableController;

public class TimerSettingsController extends BindableController<TimerSettingsModel, TimerSettingsView> {

    public TimerSettingsController(TimerSettingsModel model, TimerSettingsView view) {
        super(model, view, true);
    }

    @Override
    protected void bind(TimerSettingsModel model, TimerSettingsView view) {
        view.consoleProperty().bindBidirectional(model.consoleProperty());
        view.refreshIntervalProperty().bindBidirectional(model.refreshIntervalProperty());
        view.precisionCalibrationModeProperty().bindBidirectional(model.precisionCalibrationModeProperty());
    }

    @Override
    protected void unbind(TimerSettingsModel model, TimerSettingsView view) {
        view.consoleProperty().unbindBidirectional(model.consoleProperty());
        view.refreshIntervalProperty().unbindBidirectional(model.refreshIntervalProperty());
        view.precisionCalibrationModeProperty().unbindBidirectional(model.precisionCalibrationModeProperty());
    }
}

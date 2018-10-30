package com.github.dylmeadows.eontimer.ui.settings;

public class EonTimerSettingsController {

    /*private ActionSettingsController actionSettingsController;
    private TimerSettingsController timerSettingsController;
    private ThemeSettingsController themeSettingsController;

    public EonTimerSettingsController(EonTimerSettingsModel model, EonTimerSettingsView view) {
        actionSettingsController = new ActionSettingsController(model.getActionSettingsModel(), view.getActionSettingsView());
        timerSettingsController = new TimerSettingsController(model.getTimerSettingsModel(), view.getTimerSettingsView());
        themeSettingsController = new ThemeSettingsController(model.getThemeSettingsModel(), view.getThemeSettingsView());
        *//*modelProperty().addListener(this::onModelChange);
        viewProperty().addListener(this::onViewChange);*//*
    }

    public void onModelChange(ObservableValue<? extends EonTimerSettingsModel> observable, EonTimerSettingsModel oldModel, EonTimerSettingsModel newModel) {
        *//*actionSettingsController.setModel(newModel.getActionSettingsModel());
        timerSettingsController.setModel(newModel.getTimerSettingsModel());
        themeSettingsController.setModel(newModel.getThemeSettingsModel());*//*
    }

    public void onViewChange(ObservableValue<? extends EonTimerSettingsView> observable, EonTimerSettingsView oldView, EonTimerSettingsView newView) {
        *//*actionSettingsController.setView(newView.getActionSettingsView());
        timerSettingsController.setView(newView.getTimerSettingsView());
        themeSettingsController.setView(newView.getThemeSettingsView());*//*
    }*/
}

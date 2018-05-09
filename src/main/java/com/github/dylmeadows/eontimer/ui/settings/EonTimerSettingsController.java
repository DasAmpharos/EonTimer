package com.github.dylmeadows.eontimer.ui.settings;

import com.github.dylmeadows.common.javafx.Controller;
import com.github.dylmeadows.common.javafx.ModelChangeListener;
import com.github.dylmeadows.common.javafx.ViewChangeListener;
import com.github.dylmeadows.eontimer.ui.settings.action.ActionSettingsController;
import com.github.dylmeadows.eontimer.ui.settings.theme.ThemeSettingsController;
import com.github.dylmeadows.eontimer.ui.settings.timer.TimerSettingsController;
import javafx.beans.value.ObservableValue;

public class EonTimerSettingsController extends Controller<EonTimerSettingsModel, EonTimerSettingsView> implements
        ModelChangeListener<EonTimerSettingsModel>,
        ViewChangeListener<EonTimerSettingsView> {

    private ActionSettingsController actionSettingsController;
    private TimerSettingsController timerSettingsController;
    private ThemeSettingsController themeSettingsController;

    public EonTimerSettingsController(EonTimerSettingsModel model, EonTimerSettingsView view) {
        super(model, view);
        actionSettingsController = new ActionSettingsController(model.getActionSettingsModel(), view.getActionSettingsView());
        timerSettingsController = new TimerSettingsController(model.getTimerSettingsModel(), view.getTimerSettingsView());
        themeSettingsController = new ThemeSettingsController(model.getThemeSettingsModel(), view.getThemeSettingsView());
        modelProperty().addListener(this::onModelChange);
        viewProperty().addListener(this::onViewChange);
    }

    @Override
    public void onModelChange(ObservableValue<? extends EonTimerSettingsModel> observable, EonTimerSettingsModel oldModel, EonTimerSettingsModel newModel) {
        actionSettingsController.setModel(newModel.getActionSettingsModel());
        timerSettingsController.setModel(newModel.getTimerSettingsModel());
        themeSettingsController.setModel(newModel.getThemeSettingsModel());
    }

    @Override
    public void onViewChange(ObservableValue<? extends EonTimerSettingsView> observable, EonTimerSettingsView oldView, EonTimerSettingsView newView) {
        actionSettingsController.setView(newView.getActionSettingsView());
        timerSettingsController.setView(newView.getTimerSettingsView());
        themeSettingsController.setView(newView.getThemeSettingsView());
    }
}

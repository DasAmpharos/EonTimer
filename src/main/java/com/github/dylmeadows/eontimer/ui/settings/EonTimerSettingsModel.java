package com.github.dylmeadows.eontimer.ui.settings;

import com.github.dylmeadows.eontimer.ui.settings.action.ActionSettingsModel;
import com.github.dylmeadows.eontimer.ui.settings.theme.ThemeSettingsModel;
import com.github.dylmeadows.eontimer.ui.settings.timer.TimerSettingsModel;

public class EonTimerSettingsModel {

    private ActionSettingsModel actionSettingsModel;
    private TimerSettingsModel timerSettingsModel;
    private ThemeSettingsModel themeSettingsModel;

    public EonTimerSettingsModel() {
        actionSettingsModel = new ActionSettingsModel();
        timerSettingsModel = new TimerSettingsModel();
        themeSettingsModel = new ThemeSettingsModel();
    }

    public ActionSettingsModel getActionSettingsModel() {
        return actionSettingsModel;
    }

    public void setActionSettingsModel(ActionSettingsModel actionSettingsModel) {
        this.actionSettingsModel = actionSettingsModel;
    }

    public TimerSettingsModel getTimerSettingsModel() {
        return timerSettingsModel;
    }

    public void setTimerSettingsModel(TimerSettingsModel timerSettingsModel) {
        this.timerSettingsModel = timerSettingsModel;
    }

    public ThemeSettingsModel getThemeSettingsModel() {
        return themeSettingsModel;
    }

    public void setThemeSettingsModel(ThemeSettingsModel themeSettingsModel) {
        this.themeSettingsModel = themeSettingsModel;
    }
}

package com.github.dylmeadows.eontimer.model;

import lombok.Data;

@Data
public class ApplicationSettings {

    private final Gen3TimerModel gen3 = new Gen3TimerModel();
    private final Gen4TimerModel gen4 = new Gen4TimerModel();
    private final Gen5TimerModel gen5 = new Gen5TimerModel();
    private final CustomTimerModel custom = new CustomTimerModel();

    private final ActionSettingsModel actionSettings = new ActionSettingsModel();
    private final TimerSettingsModel timerSettings = new TimerSettingsModel();
    private final ThemeSettingsModel themeSettings = new ThemeSettingsModel();
}

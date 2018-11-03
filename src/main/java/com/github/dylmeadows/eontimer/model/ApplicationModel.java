package com.github.dylmeadows.eontimer.model;

import com.github.dylmeadows.eontimer.model.config.ActionConfigurationModel;
import com.github.dylmeadows.eontimer.model.config.ThemeConfigurationModel;
import com.github.dylmeadows.eontimer.model.config.TimerConfigurationModel;
import com.github.dylmeadows.eontimer.model.timer.CustomTimerModel;
import com.github.dylmeadows.eontimer.model.timer.Gen3TimerModel;
import com.github.dylmeadows.eontimer.model.timer.Gen4TimerModel;
import com.github.dylmeadows.eontimer.model.timer.Gen5TimerModel;
import lombok.Data;

@Data
public class ApplicationModel {

    private final Gen3TimerModel gen3 = new Gen3TimerModel();
    private final Gen4TimerModel gen4 = new Gen4TimerModel();
    private final Gen5TimerModel gen5 = new Gen5TimerModel();
    private final CustomTimerModel custom = new CustomTimerModel();

    private final ActionConfigurationModel actionSettings = new ActionConfigurationModel();
    private final TimerConfigurationModel timerSettings = new TimerConfigurationModel();
    private final ThemeConfigurationModel themeSettings = new ThemeConfigurationModel();
}

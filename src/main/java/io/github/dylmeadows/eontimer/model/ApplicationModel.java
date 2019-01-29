package io.github.dylmeadows.eontimer.model;

import io.github.dylmeadows.eontimer.model.config.ActionConfigurationModel;
import io.github.dylmeadows.eontimer.model.config.TimerConfigurationModel;
import io.github.dylmeadows.eontimer.model.timer.CustomTimerModel;
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerModel;
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerModel;
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerModel;
import lombok.Data;

@Data
public class ApplicationModel {

    private final Gen3TimerModel gen3 = new Gen3TimerModel();
    private final Gen4TimerModel gen4 = new Gen4TimerModel();
    private final Gen5TimerModel gen5 = new Gen5TimerModel();
    private final CustomTimerModel custom = new CustomTimerModel();

    private final ActionConfigurationModel actionSettings = new ActionConfigurationModel();
    private final TimerConfigurationModel timerSettings = new TimerConfigurationModel();
}

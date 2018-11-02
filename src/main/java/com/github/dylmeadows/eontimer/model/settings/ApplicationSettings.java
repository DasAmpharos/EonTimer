package com.github.dylmeadows.eontimer.model.settings;

import com.github.dylmeadows.eontimer.ui.timers.custom.CustomTimerModel;
import com.github.dylmeadows.eontimer.model.Gen3TimerModel;
import com.github.dylmeadows.eontimer.ui.timers.gen4.Gen4TimerModel;
import com.github.dylmeadows.eontimer.model.Gen5TimerModel;
import lombok.Data;

@Data
public class ApplicationSettings {

    private final Gen3TimerModel gen3 = new Gen3TimerModel();
    private final Gen4TimerModel gen4 = new Gen4TimerModel();
    private final Gen5TimerModel gen5 = new Gen5TimerModel();
    private final CustomTimerModel custom = new CustomTimerModel();
}

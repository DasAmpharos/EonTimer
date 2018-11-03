package com.github.dylmeadows.eontimer.core.timer;

import com.github.dylmeadows.eontimer.model.Timer;
import com.github.dylmeadows.eontimer.model.config.TimerConfigurationModel;
import com.github.dylmeadows.eontimer.model.timer.Gen5TimerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Gen5TimerFactory implements TimerFactory {

    private final Gen5TimerModel timerModel;
    private final TimerConfigurationModel timerConfig;

    @Autowired
    public Gen5TimerFactory(
            final Gen5TimerModel timerModel,
            final TimerConfigurationModel timerConfig) {
        this.timerModel = timerModel;
        this.timerConfig = timerConfig;
    }

    @Override
    public Timer createTimer() {
        return null;
    }
}

package com.github.dylmeadows.eontimer.core.timer;

import com.github.dylmeadows.eontimer.model.Stage;
import com.github.dylmeadows.eontimer.model.Timer;
import com.github.dylmeadows.eontimer.model.timer.CustomTimerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomTimerFactory implements TimerFactory {

    private final CustomTimerModel timerModel;

    @Autowired
    public CustomTimerFactory(final CustomTimerModel timerModel) {
        this.timerModel = timerModel;
    }

    @Override
    public Timer createTimer() {
        return new Timer(getStages());
    }

    private Stage[] getStages() {
        return timerModel.getStages()
                .stream()
                .map(Stage::new)
                .toArray(Stage[]::new);
    }
}

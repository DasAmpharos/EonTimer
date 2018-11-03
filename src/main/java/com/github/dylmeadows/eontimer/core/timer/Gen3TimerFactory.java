package com.github.dylmeadows.eontimer.core.timer;

import com.github.dylmeadows.eontimer.model.Stage;
import com.github.dylmeadows.eontimer.model.Timer;
import com.github.dylmeadows.eontimer.model.config.TimerConfigurationModel;
import com.github.dylmeadows.eontimer.model.timer.Gen3TimerModel;
import com.github.dylmeadows.eontimer.util.CalibrationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Gen3TimerFactory implements TimerFactory {

    private final Gen3TimerModel timerModel;
    private final TimerConfigurationModel timerConfig;

    @Autowired
    public Gen3TimerFactory(
            final Gen3TimerModel timerModel,
            final TimerConfigurationModel timerConfig) {
        this.timerModel = timerModel;
        this.timerConfig = timerConfig;
    }

    @Override
    public Timer createTimer() {
        Timer timer = Timer.NULL_TIMER;
        switch (timerModel.getMode()) {
            case STANDARD:
                timer = new Timer(getStages());
                break;
//            case VARIABLE_TARGET:
//                return new VariableTargetFrameTimer(
//                        model.getConsole());
        }
        return timer;
    }

    private Stage[] getStages() {
        List<Integer> stages = new ArrayList<>();
        stages.add(timerModel.getPreTimer());
        stages.add(CalibrationUtils.convertToMillis(timerModel.getTargetFrame(), timerConfig.getConsole()) + timerModel.getCalibration());
        return stages.stream()
                .map(Stage::new)
                .toArray(Stage[]::new);
    }
}

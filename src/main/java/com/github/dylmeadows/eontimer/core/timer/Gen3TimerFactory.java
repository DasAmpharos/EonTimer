package com.github.dylmeadows.eontimer.core.timer;

import com.github.dylmeadows.eontimer.model.Stage;
import com.github.dylmeadows.eontimer.model.config.TimerConfigurationModel;
import com.github.dylmeadows.eontimer.model.timer.Gen3TimerMode;
import com.github.dylmeadows.eontimer.model.timer.Gen3TimerModel;
import com.github.dylmeadows.eontimer.util.CalibrationUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Gen3TimerFactory implements TimerFactory {

    private final Gen3TimerModel timerModel;
    private final TimerConfigurationModel timerConfig;

    @Override
    public List<Stage> createTimer() {
        if (timerModel.getMode() == Gen3TimerMode.STANDARD) {
            return getStages();
//            case VARIABLE_TARGET:
//                return new VariableTargetFrameTimer(
//                        model.getConsole());
        }
        return Collections.emptyList();
    }

    private List<Stage> getStages() {
        List<Integer> stages = new ArrayList<>();
        stages.add(timerModel.getPreTimer());
        stages.add(CalibrationUtils.convertToMillis(timerModel.getTargetFrame(), timerConfig.getConsole()) + timerModel.getCalibration());
        return stages.stream()
            .map(Stage::new)
            .collect(Collectors.toList());
    }
}

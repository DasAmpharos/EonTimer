package io.github.dylmeadows.eontimer.core.timer;

import io.github.dylmeadows.eontimer.model.Stage;
import io.github.dylmeadows.eontimer.model.config.TimerConfigurationModel;
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerMode;
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerModel;
import io.github.dylmeadows.eontimer.util.Calibrations;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class Gen4TimerFactory implements TimerFactory {

    private final Gen4TimerModel timerModel;
    private final TimerConfigurationModel timerConfig;

    @Override
    public List<Stage> createTimer() {
        if (timerModel.getMode() == Gen4TimerMode.STANDARD) {
            return getStages();
        }
        return Collections.emptyList();
    }

    private List<Stage> getStages() {
        List<Integer> stages = new ArrayList<>();
        int calibration = Calibrations.createCalibration(
            timerModel.getCalibratedDelay(),
            timerModel.getCalibratedSecond(),
            timerConfig.getConsole());
        stages.add(normalize(normalize(timerModel.getTargetSecond() * 1000 + calibration + 200)
            - Calibrations.convertToMillis(timerModel.getTargetDelay(), timerConfig.getConsole())));
        stages.add(Calibrations.convertToMillis(timerModel.getTargetDelay(), timerConfig.getConsole()) - calibration);
        return stages.stream()
            .map(Stage::new)
            .collect(Collectors.toList());
    }

    private int normalize(int stage) {
        while (stage < timerConfig.getMinimumLength())
            stage += 60000;
        return stage;
    }
}

package io.github.dylmeadows.eontimer.core.timer;

import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel;
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerModel;
import io.github.dylmeadows.eontimer.service.factory.TimerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.dylmeadows.eontimer.util.CalibrationUtilsKt.convertToMillis;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class Gen3TimerFactory implements TimerFactory {

    private final Gen3TimerModel timerModel;
    private final TimerSettingsModel timerConfig;

    @Override
    public List<Long> createTimer() {
        switch (timerModel.getMode()) {
            case STANDARD:
                return Arrays.stream(getStages()).boxed()
                    .collect(Collectors.toList());
            case VARIABLE_TARGET:
            default:
                return Collections.emptyList();
        }
    }

    private long[] getStages() {
        return new long[]{
            timerModel.getPreTimer(),
            convertToMillis(timerModel.getTargetFrame(), timerConfig.getConsole())
                + timerModel.getCalibration()
        };
    }
}

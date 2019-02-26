package io.github.dylmeadows.eontimer.core.timer;

import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel;
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.dylmeadows.eontimer.util.CalibrationUtilsKt.convertToMillis;
import static io.github.dylmeadows.eontimer.util.CalibrationUtilsKt.createCalibration;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class Gen4TimerFactory implements TimerFactory {

    private final Gen4TimerModel timerModel;
    private final TimerSettingsModel timerConfig;

    @Override
    public List<Long> createTimer() {
        switch (timerModel.getMode()) {
            case STANDARD:
                return Arrays.stream(getStages()).boxed()
                    .collect(Collectors.toList());
            default:
                return Collections.emptyList();
        }
    }

    private long[] getStages() {
        int calibration = createCalibration(
            timerModel.getCalibratedDelay(),
            timerModel.getCalibratedSecond(),
            timerConfig.getConsole());
        return new long[]{
            normalize(normalize(timerModel.getTargetSecond() * 1000 + calibration + 200)
                - convertToMillis(timerModel.getTargetDelay(), timerConfig.getConsole())),
            convertToMillis(timerModel.getTargetDelay(), timerConfig.getConsole())
                - calibration
        };
    }

    private int normalize(int stage) {
        while (stage < timerConfig.getMinimumLength())
            stage += 60000;
        return stage;
    }
}

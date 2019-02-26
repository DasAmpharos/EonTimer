package io.github.dylmeadows.eontimer.core.timer;

import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel;
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class Gen5TimerFactory implements TimerFactory {

    private final Gen5TimerModel timerModel;
    private final TimerSettingsModel timerConfig;

    @Override
    public List<Long> createTimer() {
        return Collections.emptyList();
    }
}

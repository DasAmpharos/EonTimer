package io.github.dylmeadows.eontimer.core.timer;

import io.github.dylmeadows.eontimer.model.Stage;
import io.github.dylmeadows.eontimer.model.config.TimerConfigurationModel;
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class Gen5TimerFactory implements TimerFactory {

    private final Gen5TimerModel timerModel;
    private final TimerConfigurationModel timerConfig;

    @Override
    public List<Stage> createTimer() {
        return null;
    }
}

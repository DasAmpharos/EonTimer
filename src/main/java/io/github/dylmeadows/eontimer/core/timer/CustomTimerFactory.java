package io.github.dylmeadows.eontimer.core.timer;

import io.github.dylmeadows.eontimer.model.Stage;
import io.github.dylmeadows.eontimer.model.timer.CustomTimerModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CustomTimerFactory implements TimerFactory {

    private final CustomTimerModel timerModel;

    @Override
    public List<Stage> createTimer() {
        return getStages();
    }

    private List<Stage> getStages() {
        return timerModel.getStages()
            .stream()
            .map(Stage::new)
            .collect(Collectors.toList());
    }
}

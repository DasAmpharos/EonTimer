package io.github.dylmeadows.eontimer.core;

import io.github.dylmeadows.eontimer.model.Stage;
import io.github.dylmeadows.eontimer.model.Timer;
import io.github.dylmeadows.eontimer.model.config.TimerConfigurationModel;
import io.github.dylmeadows.common.core.util.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkState;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TimerRunner {

    private Thread thread;
    private final Timer timer;
    private final TimerState timerState;
    private final TimerConfigurationModel timerConfig;
    private final TimerLifeCycleEmitter emitter;

    @PostConstruct
    private void initialize() {
    }

    public void start() {
        checkState(!timerState.isRunning(), "cannot start a timer that is already running");

        resetState();
        thread = new Thread(this::run);
        timerState.setRunning(true);
        thread.start();
    }

    public void stop() {
        checkState(timerState.isRunning(), "cannot stop a timer that is not running");
        thread.interrupt();
    }

    private void run() {

    }

    private void runStage(Stage stage) throws InterruptedException {
        long endTime = System.currentTimeMillis() + stage.getLength();
        while (timerState.getRemaining() > 0) {
            timerState.setRemaining(endTime - System.currentTimeMillis());
            TimeUnit.MILLISECONDS.sleep(timerConfig.getRefreshInterval());
        }
    }

    private void resetState() {
        List<Stage> stages = timer.getStages();
        timerState.setCurrentStage(getStage(stages, 0));
        timerState.setRemaining(timerState.getCurrentStage().getLength());
        timerState.setNextStage(getStage(stages, 1));
    }

    private Stage getStage(List<Stage> stages, int index) {
        return Option.of(stages)
            .filter(list -> list.size() > index)
            .map(list -> list.get(index))
            .orElse(Stage.NULL_STAGE);
    }
}

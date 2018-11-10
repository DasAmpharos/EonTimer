package com.github.dylmeadows.eontimer.core;

import com.github.dylmeadows.eontimer.model.Stage;
import com.github.dylmeadows.eontimer.model.Timer;
import com.github.dylmeadows.eontimer.model.TimerMasterModel;
import com.github.dylmeadows.eontimer.model.config.TimerConfigurationModel;
import com.github.dylmeadows.eontimer.util.Value;
import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.github.dylmeadows.java.util.function.FunctionalUtils.asFunction;
import static com.google.common.base.Preconditions.checkState;

@Slf4j
@Component
@SuppressWarnings("unchecked")
public class TimerRunner {

    private Thread thread;
    private final TimerState timerState;
    private final TimerMasterModel timerModel;
    private final TimerConfigurationModel timerConfig;
    private final TimerLifeCycleEmitter emitter;

    @Autowired
    public TimerRunner(
        final TimerState timerState,
        final TimerMasterModel timerModel,
        final TimerConfigurationModel timerConfig,
        final TimerLifeCycleEmitter emitter) {
        this.timerState = timerState;
        this.timerModel = timerModel;
        this.timerConfig = timerConfig;
        this.emitter = emitter;
    }

    @PostConstruct
    private void initialize() {
    }

    public void start() {
        checkState(!timerState.isRunning(), "cannot start timer that is already running");

        List<Stage> stages = getStages();
        final Value<Long> endTime = new Value<>();
        final Value<Integer> stageIndex = new Value<>(0);
        timerState.setCurrentStage(getStage(stages, stageIndex.get()));
        timerState.setNextStage(getStage(stages, stageIndex.get() + 1));
        timerState.setRemaining(timerState.getCurrentStage().getLength());

        Observable.timer(1000, TimeUnit.MILLISECONDS)
            .doOnNext(elapsed -> log.info("elapsed: {}ms", elapsed))
            .subscribe();

//        Timeline timeline = new Timeline();
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(20), event -> {
//            if (endTime.get() == null) {
//                endTime.set(timerState.getCurrentStage().getLength() + System.currentTimeMillis());
//                log.info("endTime: {}ms", endTime.get());
//            } else {
//                long r = endTime.get() - System.currentTimeMillis();
//                if (r <= 0) {
//                    stageIndex.set(stageIndex.get() + 1);
//                    timerState.setCurrentStage(getStage(stages, stageIndex.get()));
//                    if (!timerState.getCurrentStage().equals(Stage.NULL_STAGE)) {
//                        endTime.set(timerState.getCurrentStage().getLength() + System.currentTimeMillis());
//                        timerState.setNextStage(getStage(stages, stageIndex.get() + 1));
//                        timerState.setRemaining(timerState.getCurrentStage().getLength());
//                    } else {
//                        timeline.stop();
//                    }
//                } else {
//                    timerState.setRemaining(r);
//                }
//            }
//        }));
//        timeline.playFromStart();
    }

    public void stop() {
        checkState(timerState.isRunning(), "cannot stop timer that is not running");
        thread.interrupt();
    }

    private void runStage(Stage stage) throws InterruptedException {
        long endTime = System.currentTimeMillis() + stage.getLength();
        while (timerState.getRemaining() > 0) {
            timerState.setRemaining(endTime - System.currentTimeMillis());
            TimeUnit.MILLISECONDS.sleep(timerConfig.getRefreshInterval());
        }
    }

    private void resetState() {
        List<Stage> stages = getStages();
        timerState.setCurrentStage(getStage(stages, 0));
        timerState.setRemaining(timerState.getCurrentStage().getLength());
        timerState.setNextStage(getStage(stages, 1));
    }

    private List<Stage> getStages() {
        return Optional.of(timerModel)
            .map(TimerMasterModel::getTimer)
            .map(Timer::getStages)
            .orElseGet(Collections::emptyList);
    }

    private Stage getStage(List<Stage> stages, int index) {
        return Optional.of(stages)
            .filter(stageList -> Optional.of(stageList)
                .map(List::size)
                .filter(size -> size > index)
                .isPresent())
            .map(asFunction(List::get, index))
            .orElse(Stage.NULL_STAGE);
    }
}

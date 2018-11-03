package com.github.dylmeadows.eontimer.core;

import com.github.dylmeadows.eontimer.model.config.TimerConfigurationModel;
import com.github.dylmeadows.eontimer.model.timer.TimerConstants;
import javafx.beans.value.ObservableValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class TimerRunner {

    private final TimerConfigurationModel timerConfig;
    private final List<TimerLifeCycleListener> timerLifeCycleListeners;
    private final List<StageLifeCycleListener> stageLifeCycleListeners;

    @Autowired
    public TimerRunner(final TimerConfigurationModel timerConfig) {
        this.timerConfig = timerConfig;
        this.timerLifeCycleListeners = new ArrayList<>();
        this.stageLifeCycleListeners = new ArrayList<>();
    }

    public void run() {
        if (thread == null || !thread.isAlive()) {
            timerLifeCycleListeners.forEach(TimerLifeCycleListener::onStart);
            try {
                for (int i = 0; i < getTimer().getStages().size(); i++)
                    runStage(i);
            } catch (InterruptedException e) {
            }
//            if (getTimer() instanceof VariableTimer)
//                ((VariableTimer) getTimer()).reset();
            timerLifeCycleListeners.forEach(TimerLifeCycleListener::onEnd);
//            setRunning(false);
//            setRunning(true);
        }
    }

    private void runStage(int stageIndex) throws InterruptedException {
        LocalDateTime now = LocalDateTime.now();

        // sleep while stage is infinite
        Duration stage;
        while ((stage = getTimer().getStages().get(stageIndex)).equals(TimerConstants.INFINITE_TIME_SPAN)) {
            TimeUnit.MILLISECONDS.sleep(timerConfig.getRefreshInterval());
            System.out.println(stage);
        }

        LocalDateTime endTime = now.plus(stage);
        Duration remaining = Duration.between(now, endTime);

        for (StageLifeCycleListener listener : stageLifeCycleListeners)
            listener.onStageStart(stage);

        while (remaining.toMillis() > 0) {
            TimeUnit.MILLISECONDS.sleep(timerConfig.getRefreshInterval());
            remaining = Duration.between(LocalDateTime.now(), endTime);
            for (StageLifeCycleListener listener : stageLifeCycleListeners)
                listener.onStageUpdate(stage, remaining);
        }

        for (StageLifeCycleListener listener : stageLifeCycleListeners) {
            listener.onStageEnd(stage);
        }
    }

    public void cancel() {
        if (state.isTimerRunning()) {

            thread.interrupt();
            // spin until the thread has stopped running
            setRunning(false);

            timerLifeCycleListeners.forEach(TimerLifeCycleListener::onEnd);
        }
    }

    public void addListener(TimerLifeCycleListener listener) {
        timerLifeCycleListeners.add(listener);
    }

    public void addListener(StageLifeCycleListener handler) {
        stageLifeCycleListeners.add(handler);
    }

    public void clearListeners() {
        timerLifeCycleListeners.clear();
        stageLifeCycleListeners.clear();
    }
}

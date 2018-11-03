package com.github.dylmeadows.eontimer.core;

import com.github.dylmeadows.eontimer.model.timer.TimerConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class DisplayHandler implements TimerLifeCycleListener, StageLifeCycleListener {

    private final TimerRunner timerRunner;

    @Autowired
    public DisplayHandler(final TimerRunner timerRunner) {
        this.timerRunner = timerRunner;
    }

    @Override
    public void onSetup() {
        Timer timer = timerRunner.getTimer();
        if (timer.getStages() != null) {
            List<Duration> stages = timerRunner.getTimer().getStages();
            if (stages.size() > 0) {
                Duration currentStage = stages.get(0);
                setCurrentStage(formatTime(currentStage));
            }
            Duration nextStage = (stages.size() > 1) ? stages.get(1) : TimerConstants.NULL_TIME_SPAN;
            setNextStage(formatTime(nextStage));
        }
        int minutesBeforeTarget = timer.getMinutesBeforeTarget();
        setMinutesBeforeTarget(Integer.toString(minutesBeforeTarget));
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onEnd() {
        onSetup();
    }

    @Override
    public void onStageStart(long stage) {
        long duration = TimerConstants.NULL_TIME_SPAN;
        List<Long> stages = timerRunner.getTimer().getStages();
        if (stages.indexOf(stage) != -1) {
            int nextStageIndex = stages.indexOf(stage) + 1;
            if (stages.size() > nextStageIndex)
                duration = stages.get(nextStageIndex);
            setNextStage(formatTime(duration));
        }
    }

    @Override
    public void onStageUpdate(long stage, long remaining) {
        setCurrentStage(formatTime(remaining));
    }

    @Override
    public void onStageEnd(long stage) {
    }

    private String formatTime(long duration) {
        if (duration == TimerConstants.NULL_TIME_SPAN)
            return "0:00";
        else if (duration == TimerConstants.INFINITE_TIME_SPAN)
            return "?:??";
        else {
            int seconds = ((int) duration / 1000);
            int millis = ((int) (duration / 10) % 100);
            return String.format("%d:%02d", seconds, millis);
        }
    }
}

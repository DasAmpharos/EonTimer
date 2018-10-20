package com.github.dylmeadows.eontimer.handlers;

import java.time.Duration;

public class DisplayHandler implements TimerLifecycleListener, TimerStageLifecycleListener {

    // TODO: refactor

    @Override
    public void onSetup() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onEnd() {

    }

    @Override
    public void onStageStart(Duration stage) {

    }

    @Override
    public void onStageUpdate(Duration stage, Duration remaining) {

    }

    @Override
    public void onStageEnd(Duration stage) {

    }


    /*private ITimerMonitor monitor;

    private final ReadOnlyStringWrapper nextStage;

    private final ReadOnlyStringWrapper currentStage;

    private final ReadOnlyStringWrapper minutesBeforeTarget;

    public DisplayHandler(ITimerMonitor monitor) {
        this.monitor = monitor;
        this.nextStage = new ReadOnlyStringWrapper("0:00");
        this.currentStage = new ReadOnlyStringWrapper("0:00");
        this.minutesBeforeTarget = new ReadOnlyStringWrapper("0");
    }

    @Override
    public void onSetup() {
        Timer timer = monitor.getTimer();
        if (timer.getStages() != null) {
            List<Duration> stages = monitor.getTimer().getStages();
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
    public void onStageStart(Duration stage) {
        Duration duration = TimerConstants.NULL_TIME_SPAN;
        List<Duration> stages = monitor.getTimer().getStages();
        if (stages.indexOf(stage) != -1) {
            int nextStageIndex = stages.indexOf(stage) + 1;
            if (stages.size() > nextStageIndex)
                duration = stages.get(nextStageIndex);
            setNextStage(formatTime(duration));
        }
    }

    @Override
    public void onStageUpdate(Duration stage, Duration remaining) {
        setCurrentStage(formatTime(remaining));
    }

    @Override
    public void onStageEnd(Duration stage) {
    }

    *//**
     * Formats the duration parameter as a string containing the total seconds
     * and milliseconds of the duration.
     *
     * @param duration the value to format
     * @return a {@link String} containing the total seconds and milliseconds
     * left in the duration parameter
     *//*
    private String formatTime(Duration duration) {
        if (duration == TimerConstants.NULL_TIME_SPAN)
            return "0:00";
        else if (duration == TimerConstants.INFINITE_TIME_SPAN)
            return "?:??";
        else {
            int seconds = ((int) duration.toMillis() / 1000);
            int millis = ((int) (duration.toMillis() / 10) % 100);
            return String.format("%d:%02d", seconds, millis);
        }
    }

    public String getNextStage() {
        return nextStage.get();
    }

    public ReadOnlyStringProperty nextStageProperty() {
        return nextStage.getReadOnlyProperty();
    }

    private void setNextStage(String nextStage) {
        if (!Platform.isFxApplicationThread())
            Platform.runLater(() -> this.nextStage.set(nextStage));
        else
            this.nextStage.set(nextStage);
    }

    public String getCurrentStage() {
        return currentStage.get();
    }

    public ReadOnlyStringProperty currentStageProperty() {
        return currentStage.getReadOnlyProperty();
    }

    private void setCurrentStage(String currentStage) {
        if (!Platform.isFxApplicationThread())
            Platform.runLater(() -> this.currentStage.set(currentStage));
        else
            this.currentStage.set(currentStage);
    }

    public String getMinutesBeforeTarget() {
        return minutesBeforeTarget.get();
    }

    public ReadOnlyStringProperty minutesBeforeTargetProperty() {
        return minutesBeforeTarget.getReadOnlyProperty();
    }

    private void setMinutesBeforeTarget(String minutesBeforeTarget) {
        if (!Platform.isFxApplicationThread())
            Platform.runLater(() -> this.minutesBeforeTarget.set(minutesBeforeTarget));
        else
            this.minutesBeforeTarget.set(minutesBeforeTarget);
    }*/
}

package com.github.dylmeadows.eontimer.handlers;

import com.github.dylmeadows.eontimer.model.TimerSettingsConstants;
import com.github.dylmeadows.eontimer.model.timers.Timer;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;

import java.util.List;

/**
 * Contains the underlying {@link Timer} implementation that the TimerMonitor
 * is responsible for running and monitoring for timer events. When timer events
 * occur, registered event handlers are notified. Also contains an int
 * ({@link #refreshInterval}) that determines how much time (in ms) should elapse
 * between timer run cycles and the {@link Thread} responsible for running the
 * timer.
 */
public class TimerMonitor implements ITimerMonitor, TimerSettingsConstants, TimerChangeListener {
    // TODO: refactor

    @Override
    public void run() {

    }

    @Override
    public void cancel() {

    }

    @Override
    public void addListener(TimerLifecycleListener listener) {

    }

    @Override
    public void addListener(TimerStageLifecycleListener listener) {

    }

    @Override
    public void clearListeners() {

    }

    @Override
    public List<TimerLifecycleListener> getLifecycleListeners() {
        return null;
    }

    @Override
    public List<TimerStageLifecycleListener> getStageLifecycleListeners() {
        return null;
    }

    @Override
    public Timer getTimer() {
        return null;
    }

    @Override
    public ObjectProperty<Timer> timerProperty() {
        return null;
    }

    @Override
    public void setTimer(Timer timer) {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public ReadOnlyBooleanProperty runningProperty() {
        return null;
    }

    @Override
    public void onTimerChange(ObservableValue<? extends Timer> observable, Timer oldValue, Timer newValue) {

    }



    /*
    *//**
     * Underlying timer implementation to run and monitor for events.
     *//*
    private final ObjectProperty<Timer> timer;

    *//**
     * List of registered {@link TimerLifecycleListener}s.
     *//*
    private final List<TimerLifecycleListener> lifecycle;

    *//**
     * List of registered {@link TimerStageLifecycleListener}s.
     *//*
    private final List<TimerStageLifecycleListener> stageLifecycle;

    *//**
     * How much time (in ms) should elapse between timer run cycles.
     *//*
    private final IntegerProperty refreshInterval;

    *//**
     * Timer running state.
     *//*
    private final ReadOnlyBooleanWrapper running;

    *//**
     * Thread used to run the timer implementation.
     *//*
    private Thread thread;

    *//**
     * Creates a new TimerMonitor.
     *//*
    public TimerMonitor() {
        this.timer = new SimpleObjectProperty<>(new NullTimer());
        this.timer.addListener(this::onTimerChange);
        this.refreshInterval = new SimpleIntegerProperty(DEFAULT_REFRESH_INTERVAL);
        this.running = new ReadOnlyBooleanWrapper(false);
        this.lifecycle = new ArrayList<>();
        this.stageLifecycle = new ArrayList<>();
    }

    *//**
     * Runs the underlying timer implementation on a separate thread if the timer is not already running.
     *//*
    @Override
    public void run() {
        if (thread == null || !thread.isAlive()) {
            thread = new Thread(() -> {
                for (TimerLifecycleListener listener : lifecycle)
                    listener.onStart();
                try {
                    for (int i = 0; i < getTimer().getStages().size(); i++)
                        runStage(i);
                } catch (InterruptedException e) {
                }
                if (getTimer() instanceof VariableTimer)
                    ((VariableTimer) getTimer()).reset();
                for (TimerLifecycleListener listener : lifecycle)
                    listener.onEnd();
                setRunning(false);
            });
            thread.start();
            setRunning(true);
        }
    }

    *//**
     * Runs the given stage until the stage has ended.
     *
     * @param stageIndex the stage to run
     *//*
    private void runStage(int stageIndex) throws InterruptedException {
        LocalDateTime now = LocalDateTime.now();

        // sleep while stage is infinite
        Duration stage;
        while ((stage = getTimer().getStages().get(stageIndex)).equals(TimerConstants.INFINITE_TIME_SPAN)) {
            TimeUnit.MILLISECONDS.sleep(refreshInterval.get());
            System.out.println(stage);
        }

        LocalDateTime endTime = now.plus(stage);
        Duration remaining = Duration.between(now, endTime);

        for (TimerStageLifecycleListener listener : stageLifecycle)
            listener.onStageStart(stage);

        while (remaining.toMillis() > 0) {
            TimeUnit.MILLISECONDS.sleep(getRefreshInterval());
            remaining = Duration.between(LocalDateTime.now(), endTime);
            for (TimerStageLifecycleListener listener : stageLifecycle)
                listener.onStageUpdate(stage, remaining);
        }

        for (TimerStageLifecycleListener listener : stageLifecycle)
            listener.onStageEnd(stage);
    }

    *//**
     * Stops the timer if it is running and notifies the registered handlers.
     *//*
    @Override
    public void cancel() {
        if (isRunning()) {
            thread.interrupt();
            // spin until the thread has stopped running
            // noinspection StatementWithEmptyBody
            while (thread != null && thread.isAlive()) ;
            setRunning(false);

            for (TimerLifecycleListener listener : lifecycle)
                listener.onEnd();
        }
    }

    *//**
     * Gets if the timer is running.
     *
     * @return true if thread has been instantiated and is alive
     * @see Thread#isAlive()
     *//*
    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public ReadOnlyBooleanProperty runningProperty() {
        return running.getReadOnlyProperty();
    }

    protected void setRunning(boolean running) {
        if (!Platform.isFxApplicationThread())
            Platform.runLater(() -> this.running.set(running));
        else
            this.running.set(running);
    }

    @Override
    public void addListener(TimerLifecycleListener listener) {
        lifecycle.add(listener);
    }

    @Override
    public void addListener(TimerStageLifecycleListener handler) {
        stageLifecycle.add(handler);
    }

    *//**
     * Removes all registered handlers.
     *//*
    @Override
    public void clearListeners() {
        lifecycle.clear();
        stageLifecycle.clear();
    }

    @Override
    public void onTimerChange(ObservableValue<? extends Timer> observable, Timer oldValue, Timer newValue) {
        for (TimerLifecycleListener handler : lifecycle)
            handler.onSetup();
    }

    @Override
    public List<TimerLifecycleListener> getLifecycleListeners() {
        return lifecycle;
    }

    @Override
    public List<TimerStageLifecycleListener> getStageLifecycleListeners() {
        return stageLifecycle;
    }

    *//**
     * @return see {@link #timer}
     *//*
    @Override
    public Timer getTimer() {
        return timer.get();
    }

    @Override
    public ObjectProperty<Timer> timerProperty() {
        return timer;
    }

    *//**
     * @param timer see {@link #timer}
     *//*
    @Override
    public void setTimer(Timer timer) {
        this.timer.set(timer);
    }

    *//**
     * @return see {@link #refreshInterval}
     *//*
    public int getRefreshInterval() {
        return refreshInterval.get();
    }

    *//**
     * @return see {@link #refreshInterval}
     *//*
    public IntegerProperty refreshIntervalProperty() {
        return refreshInterval;
    }

    *//**
     * @param refreshInterval see {@link #refreshInterval}
     *//*
    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval.set(refreshInterval);
    }*/
}

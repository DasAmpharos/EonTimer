package com.github.dylmeadows.eontimer.handlers;

import com.github.dylmeadows.eontimer.model.timers.Timer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;

import java.util.List;

/**
 * Responsible for running the underlying {@link Timer} implementation and
 * notifying registered event handlers of timer events as they occur.
 */
public interface ITimerMonitor {

    /**
     * Runs the timer.
     */
    void run();

    /**
     * Cancels the timer.
     */
    void cancel();

    /**
     * Registers the given {@link TimerLifecycleListener} to the {@link List} of
     * TimerLifecycleListeners.
     *
     * @param listener the listener to register
     */
    void addListener(TimerLifecycleListener listener);

    /**
     * Registers the given {@link TimerStageLifecycleListener} to the {@link List} of
     * TimerStageLifecycleListeners.
     *
     * @param listener the listener to register
     */
    void addListener(TimerStageLifecycleListener listener);

    /**
     * Removes all registered listeners.
     */
    void clearListeners();

    /**
     * Gets a {@link List} of {@link TimerLifecycleListener}s registered.
     *
     * @return list of registered TimerLifecycleListeners
     */
    List<TimerLifecycleListener> getLifecycleListeners();

    /**
     * Gets a {@link List} of {@link TimerStageLifecycleListener}s registered.
     *
     * @return list of registered TimerStageLifecycleListeners
     */
    List<TimerStageLifecycleListener> getStageLifecycleListeners();

    /**
     * Gets the timer.
     *
     * @return current timer
     */
    Timer getTimer();

    ObjectProperty<Timer> timerProperty();

    /**
     * Sets the timer.
     *
     * @param timer new timer
     */
    void setTimer(Timer timer);

    /**
     * Gets if the timer is running.
     *
     * @return true if the timer is running, false if it is not
     */
    boolean isRunning();

    /**
     * Gets the timer running state property.
     *
     * @return timer running state property
     */
    ReadOnlyBooleanProperty runningProperty();
}

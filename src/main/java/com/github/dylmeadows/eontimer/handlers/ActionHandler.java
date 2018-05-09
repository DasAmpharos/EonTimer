package com.github.dylmeadows.eontimer.handlers;

import com.github.dylmeadows.eontimer.handlers.actions.ICountdownAction;
import com.github.dylmeadows.eontimer.reference.settings.ActionSettingsConstants;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains a {@link List} of {@link ICountdownAction}s registered to the
 * ActionHandler, an int specifying the length of time (in ms) between updating
 * the registered actions and an int specifying the total number of times to
 * update the registered actions.
 */
public class ActionHandler implements TimerStageLifecycleListener, ActionSettingsConstants {

    /**
     * Registered actions to propagate updates to when an update is received
     * from a running {@link ITimerMonitor}.
     */
    private final List<ICountdownAction> actions;

    /**
     * The length of time (in ms) between actions.
     */
    private final IntegerProperty interval;

    /**
     * The total number of times to update registered actions.
     */
    private final IntegerProperty count;

    /**
     * Internal variable used to track when the next action is supposed to
     * occur.
     */
    private Duration nextAction;

    /**
     * Instantiates an empty {@link List} of registered actions. {@link #interval}
     * and {@link #count} are initialized to default values.
     */
    public ActionHandler() {
        this(DEFAULT_INTERVAL, DEFAULT_COUNT);
    }

    /**
     * Instantiates an empty {@link List} of registered actions. {@link #interval}
     * and {@link #count} are initialized to their respective parameters.
     *
     * @param interval see {@link #interval}
     * @param count    see {@link #count}
     */
    public ActionHandler(int interval, int count) {
        this.actions = new ArrayList<>();
        this.interval = new SimpleIntegerProperty(interval);
        this.count = new SimpleIntegerProperty(count);
    }

    /**
     * Calculates the time to the next action. {@link #nextAction} stores the
     * result.
     *
     * @param stage current timer stage
     */
    @Override
    public void onStageStart(Duration stage) {
        Duration next = Duration.ofMillis(getInterval() * (getCount() - 1));
        if (stage.compareTo(next) < 0 && getInterval() != 0)
            nextAction = Duration.ofMillis(stage.toMillis());
        else
            nextAction = next;
    }

    /**
     * Determines if registered actions require notification. If registered
     * actions are notified, then {@link #nextAction} is updated.
     */
    @Override
    public void onStageUpdate(Duration stage, Duration remaining) {
        // EonTimer #1 - Last action occasionally doesn't fire
        if (remaining.compareTo(nextAction) <= 0) {
            for (ICountdownAction action : actions)
                action.action();
            nextAction = nextAction.minus(getInterval(), ChronoUnit.MILLIS);
        }
    }

    /**
     * Not implemented.
     *
     * @param stage unused
     */
    @Override
    public void onStageEnd(Duration stage) {
    }

    /**
     * @return see {@link #actions}
     */
    public List<ICountdownAction> getActions() {
        return actions;
    }

    /**
     * @return see {@link #interval}
     */
    public int getInterval() {
        return interval.get();
    }

    /**
     * @return see {@link #interval}
     */
    public IntegerProperty intervalProperty() {
        return interval;
    }

    /**
     * @param interval see {@link #interval}
     */
    public void setInterval(int interval) {
        this.interval.set(interval);
    }

    /**
     * @return see {@link #count}
     */
    public int getCount() {
        return count.get();
    }

    /**
     * @return see {@link #count}
     */
    public IntegerProperty countProperty() {
        return count;
    }

    /**
     * @param count see {@link #count}
     */
    public void setCount(int count) {
        this.count.set(count);
    }
}

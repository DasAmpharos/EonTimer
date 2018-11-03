package com.github.dylmeadows.eontimer.core;

import com.github.dylmeadows.eontimer.core.action.CountdownAction;
import com.github.dylmeadows.eontimer.model.config.ActionConfigurationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ActionHandler implements StageLifeCycleListener {

    private final ActionConfigurationModel actionSettings;
    private final List<CountdownAction> actions;
    private long nextAction;

    @Autowired
    public ActionHandler(final ActionConfigurationModel actionSettings) {
        this.actionSettings = actionSettings;
        this.actions = new ArrayList<>();
    }

    @Override
    public void onStageStart(long stage) {
        long next = actionSettings.getInterval() * (actionSettings.getCount() - 1);
        nextAction = (stage < next && actionSettings.getInterval() != 0) ? stage : next;
    }

    /**
     * Determines if registered actions require notification. If registered
     * actions are notified, then {@link #nextAction} is updated.
     */
    @Override
    public void onStageUpdate(long stage, long remaining) {
        if (remaining <= nextAction) {
            actions.forEach(CountdownAction::action);
            nextAction -= actionSettings.getInterval();
        }
    }

    /**
     * Not implemented.
     *
     * @param stage unused
     */
    @Override
    public void onStageEnd(long stage) {
    }

    public void addCountdownAction(CountdownAction action) {
        actions.add(action);
    }
}

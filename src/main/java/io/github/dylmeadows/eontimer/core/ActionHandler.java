package io.github.dylmeadows.eontimer.core;

import io.github.dylmeadows.eontimer.core.action.CountdownAction;
import io.github.dylmeadows.eontimer.model.config.ActionConfigurationModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ActionHandler {

    private final ActionConfigurationModel actionSettings;
    private final List<CountdownAction> actions = new ArrayList<>();
    private long nextAction;

    public void addCountdownAction(CountdownAction action) {
        actions.add(action);
    }

    public void onStageStart(long stage) {
        long next = actionSettings.getInterval() * (actionSettings.getCount() - 1);
        nextAction = (stage < next && actionSettings.getInterval() != 0) ? stage : next;
    }

    public void onStageUpdate(long stage, long remaining) {
        if (remaining <= nextAction) {
            actions.forEach(CountdownAction::action);
            nextAction -= actionSettings.getInterval();
        }
    }

    public void onStageEnd(long stage) {
    }
}

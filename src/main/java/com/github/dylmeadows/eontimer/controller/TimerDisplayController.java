package com.github.dylmeadows.eontimer.controller;

import com.github.dylmeadows.eontimer.core.StageLifeCycleListener;
import com.github.dylmeadows.eontimer.core.TimerLifeCycleListener;
import moe.tristan.easyfxml.api.FxmlController;
import org.springframework.stereotype.Component;

@Component
public class TimerDisplayController implements FxmlController {

    private final TimerLifeCycleListener timerLifeCycleListener;
    private final StageLifeCycleListener stageLifeCycleListener;

    @Override
    public void initialize() {

    }
}

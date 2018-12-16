package com.github.dylmeadows.eontimer.core.timer;

import com.github.dylmeadows.eontimer.model.Stage;

import java.util.List;

public interface TimerFactory {

    List<Stage> createTimer();
}

package io.github.dylmeadows.eontimer.core.timer;

import io.github.dylmeadows.eontimer.model.Stage;

import java.util.List;

public interface TimerFactory {

    List<Stage> createTimer();
}

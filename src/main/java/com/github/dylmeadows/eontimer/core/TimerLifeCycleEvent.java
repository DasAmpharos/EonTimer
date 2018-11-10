package com.github.dylmeadows.eontimer.core;

import com.github.dylmeadows.eontimer.model.Stage;
import com.github.dylmeadows.eontimer.model.Timer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class TimerLifeCycleEvent {
    private final Timer source;
    private final int minutesBeforeTarget;
    private final Stage currentStage;
    private final Stage nextStage;
    private final long remaining;
}

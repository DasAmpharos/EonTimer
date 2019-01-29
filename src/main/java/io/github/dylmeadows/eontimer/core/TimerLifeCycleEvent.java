package io.github.dylmeadows.eontimer.core;

import io.github.dylmeadows.eontimer.model.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class TimerLifeCycleEvent {
    private final int minutesBeforeTarget;
    private final Stage currentStage;
    private final Stage nextStage;
    private final long remaining;
}

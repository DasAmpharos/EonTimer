package com.github.dylmeadows.eontimer.timers;

import java.time.Duration;
import java.util.List;

public interface Timer {

    List<Duration> getStages();

    int getMinutesBeforeTarget();
}

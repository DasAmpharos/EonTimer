package com.github.dylmeadows.eontimer.reference.timer;

import java.time.Duration;

public interface TimerConstants {

    double UPDATE_FACTOR = 1.0;

    double CLOSE_UPDATE_FACTOR = 0.75;

    int CLOSE_THRESHOLD = 167;

    Duration NULL_TIME_SPAN = Duration.ofMillis(-999);

    Duration INFINITE_TIME_SPAN = Duration.ofMillis(-99);
}

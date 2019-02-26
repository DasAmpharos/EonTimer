package io.github.dylmeadows.eontimer.model.timer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimerConstants {
    public static final double UPDATE_FACTOR = 1.0;
    public static final double CLOSE_UPDATE_FACTOR = 0.75;
    public static final int CLOSE_THRESHOLD = 167;
    public static final long NULL_TIME_SPAN = -999;
    public static final long INFINITE_TIME_SPAN = -99;
    public static final TimerType DEFAULT_TIMER_TYPE = TimerType.GEN5;
}

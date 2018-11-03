package com.github.dylmeadows.eontimer.model.timer;

import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings({"unused", "WeakerAccess"})
public class TimerConstants {
    public final double UPDATE_FACTOR = 1.0;
    public final double CLOSE_UPDATE_FACTOR = 0.75;
    public final int CLOSE_THRESHOLD = 167;
    public final long NULL_TIME_SPAN = -999;
    public final long INFINITE_TIME_SPAN = -99;
}

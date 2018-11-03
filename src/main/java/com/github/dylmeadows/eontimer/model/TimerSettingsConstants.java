package com.github.dylmeadows.eontimer.model;

import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings({"unused", "WeakerAccess"})
public class TimerSettingsConstants {
    public final Console DEFAULT_CONSOLE = Console.NDS;
    public final boolean DEFAULT_PRECISION_CALIBRATION_MODE = false;
    public final int DEFAULT_REFRESH_INTERVAL = 8;
    public final int DEFAULT_MINIMUM_LENGTH = 14000;
}

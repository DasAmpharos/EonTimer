package com.github.dylmeadows.eontimer.model.timer;

import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings({"unused", "WeakerAccess"})
public class Gen5TimerConstants {
    public final Gen5TimerMode DEFAULT_MODE = Gen5TimerMode.STANDARD;
    public final int DEFAULT_CALIBRATION = -95;
    public final int DEFAULT_TARGET_DELAY = 1200;
    public final int DEFAULT_TARGET_SECOND = 50;
    public final int DEFAULT_ENTRALINK_CALIBRATION = 256;
    public final int DEFAULT_FRAME_CALIBRATION = 0;
    public final int DEFAULT_TARGET_ADVANCES = 100;
    public final double ENTRALINK_FRAME_RATE = 0.837148929;
}

package com.github.dylmeadows.eontimer.component.timer;

import com.github.dylmeadows.eontimer.model.Gen5TimerMode;
import moe.tristan.easyfxml.api.FxmlController;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings({"unused", "WeakerAccess"})
public class Gen5TimerController implements FxmlController {

    public static final Gen5TimerMode DEFAULT_MODE = Gen5TimerMode.STANDARD;
    public static final int DEFAULT_CALIBRATION = -95;
    public static final int DEFAULT_TARGET_DELAY = 1200;
    public static final int DEFAULT_TARGET_SECOND = 50;
    public static final int DEFAULT_ENTRALINK_CALIBRATION = 256;
    public static final int DEFAULT_FRAME_CALIBRATION = 0;
    public static final int DEFAULT_TARGET_ADVANCES = 100;
    public static final double ENTRALINK_FRAME_RATE = 0.837148929;

    @Override
    public void initialize() {

    }
}

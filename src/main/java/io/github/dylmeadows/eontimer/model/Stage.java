package io.github.dylmeadows.eontimer.model;

import io.github.dylmeadows.eontimer.model.timer.TimerConstants;
import lombok.Data;

@Data
@SuppressWarnings({"unused", "WeakerAccess"})
public class Stage {
    private final long length;

    public static final Stage NULL_STAGE = new Stage(TimerConstants.NULL_TIME_SPAN);
    public static final Stage INFINITE_STAGE = new Stage(TimerConstants.INFINITE_TIME_SPAN);
}

package com.github.dylmeadows.eontimer.timers;

import java.util.Collections;

public class NullTimer extends BaseTimer {

    @Override
    protected void initialize() {
        setStages(Collections.emptyList());
    }

    @Override
    public int getMinutesBeforeTarget() {
        return 0;
    }
}

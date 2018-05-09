package com.github.dylmeadows.eontimer.timers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTimer implements Timer {

    private List<Duration> stages = new ArrayList<>();

    protected void initialize() {
    }

    @Override
    public List<Duration> getStages() {
        return stages;
    }

    protected void setStages(List<Duration> stages) {
        this.stages = stages;
    }
}

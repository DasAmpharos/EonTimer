package com.github.dylmeadows.eontimer.model;

import lombok.Data;

@Data
public class Timer {

    private final Stage[] stages;

    public static final Timer NULL_TIMER = new Timer(new Stage[0]);
}

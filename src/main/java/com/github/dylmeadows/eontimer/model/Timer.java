package com.github.dylmeadows.eontimer.model;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class Timer {

    private final List<Stage> stages;

    public static final Timer EMPTY_TIMER = new Timer(Collections.emptyList());
}

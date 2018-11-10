package com.github.dylmeadows.eontimer.core;

import com.github.dylmeadows.eontimer.model.Timer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.dylmeadows.java.util.function.FunctionalUtils.asConsumer;

@Component
class TimerLifeCycleMap implements TimerLifeCycleHook {

    private final List<TimerOnInit> onInit = new ArrayList<>();
    private final List<TimerOnStart> onStart = new ArrayList<>();
    private final List<TimerOnUpdate> onUpdate = new ArrayList<>();
    private final List<TimerOnStop> onStop = new ArrayList<>();

    @Override
    public TimerLifeCycleHook onInit(TimerOnInit onInit) {
        this.onInit.add(onInit);
        return this;
    }

    @Override
    public TimerLifeCycleHook onStart(TimerOnStart onStart) {
        this.onStart.add(onStart);
        return this;
    }

    @Override
    public TimerLifeCycleHook onUpdate(TimerOnUpdate onUpdate) {
        this.onUpdate.add(onUpdate);
        return this;
    }

    @Override
    public TimerLifeCycleHook onStop(TimerOnStop onStop) {
        this.onStop.add(onStop);
        return this;
    }

    List<TimerOnInit> getOnInit() {
        return Collections.unmodifiableList(onInit);
    }

    List<TimerOnStart> getOnStart() {
        return Collections.unmodifiableList(onStart);
    }

    List<TimerOnUpdate> getOnUpdate() {
        return Collections.unmodifiableList(onUpdate);
    }

    List<TimerOnStop> getOnStop() {
        return Collections.unmodifiableList(onStop);
    }
}

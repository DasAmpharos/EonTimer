package com.github.dylmeadows.eontimer.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.github.dylmeadows.java.util.function.FunctionalUtils.asConsumer;

@Component
class TimerLifeCycleEmitter {

    private final TimerLifeCycleMap map;

    @Autowired
    public TimerLifeCycleEmitter(TimerLifeCycleMap map) {
        this.map = map;
    }

    void emitInit(TimerLifeCycleEvent initEvent) {
        map.getOnInit().forEach(asConsumer(TimerOnInit::onTimerInit, initEvent));
    }

    void emitStart(TimerLifeCycleEvent startEvent) {
        map.getOnStart().forEach(asConsumer(TimerOnStart::onTimerStart, startEvent));
    }

    void emitUpdate(TimerLifeCycleEvent updateEvent) {
        map.getOnUpdate().forEach(asConsumer(TimerOnUpdate::onTimerUpdate, updateEvent));
    }

    void emitStop(TimerLifeCycleEvent stopEvent) {
        map.getOnStop().forEach(asConsumer(TimerOnStop::onTimerStop, stopEvent));
    }
}

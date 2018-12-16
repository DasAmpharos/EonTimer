package com.github.dylmeadows.eontimer.core;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Component
@RequiredArgsConstructor(access = PRIVATE, onConstructor = @__(@Autowired))
@SuppressWarnings("unused")
class TimerLifeCycleEmitter {

    private final TimerLifeCycleMap map;

    void emitInit(TimerLifeCycleEvent initEvent) {
        map.getOnInit().forEach(timerOnInit -> timerOnInit.onTimerInit(initEvent));
    }

    void emitStart(TimerLifeCycleEvent startEvent) {
        map.getOnStart().forEach(timerOnStart -> timerOnStart.onTimerStart(startEvent));
    }

    void emitUpdate(TimerLifeCycleEvent updateEvent) {
        map.getOnUpdate().forEach(timerOnUpdate -> timerOnUpdate.onTimerUpdate(updateEvent));
    }

    void emitStop(TimerLifeCycleEvent stopEvent) {
        map.getOnStop().forEach(timerOnStop -> timerOnStop.onTimerStop(stopEvent));
    }
}

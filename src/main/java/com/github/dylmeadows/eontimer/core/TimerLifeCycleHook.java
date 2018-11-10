package com.github.dylmeadows.eontimer.core;

public interface TimerLifeCycleHook {

    TimerLifeCycleHook onInit(TimerOnInit onInit);

    TimerLifeCycleHook onStart(TimerOnStart onStart);

    TimerLifeCycleHook onUpdate(TimerOnUpdate onUpdate);

    TimerLifeCycleHook onStop(TimerOnStop onStop);
}

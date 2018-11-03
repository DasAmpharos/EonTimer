package com.github.dylmeadows.eontimer.core;

@SuppressWarnings("unused")
public interface TimerLifeCycleListener {

    void onSetup();

    void onStart();

    void onEnd();
}

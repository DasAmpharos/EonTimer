package com.github.dylmeadows.eontimer.handlers;

@SuppressWarnings("unused")
public interface TimerLifecycleListener {

    /**
     * Timer event fired when the underlying timer implementation has completed
     * setup.
     */
    void onSetup();

    /**
     * Timer event fired when the underlying timer implementation has started.
     */
    void onStart();

    /**
     * Timer event fired when the underlying timer implementation has ended.
     */
    void onEnd();
}

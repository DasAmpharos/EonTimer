package com.github.dylmeadows.eontimer.handlers;

public abstract class TimerLifecycleAdapter implements TimerLifecycleListener {

    /**
     * Timer event fired when the underlying timer implementation has completed
     * setup.
     */
    @Override
    public void onSetup() {
    }

    /**
     * Timer event fired when the underlying timer implementation has started.
     */
    @Override
    public void onStart() {
    }

    /**
     * Timer event fired when the underlying timer implementation has ended.
     */
    @Override
    public void onEnd() {
    }
}

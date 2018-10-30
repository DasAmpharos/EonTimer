package com.github.dylmeadows.eontimer.handlers;

import java.time.Duration;

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class TimerStageLifecycleAdapter implements TimerStageLifecycleListener {

    /**
     * Timer event fired when the underlying timer implementation has started
     * a stage. The stage the timer started is passed as a parameter.
     *
     * @param stage the stage started by the timer
     */
    public void onStageStart(Duration stage) {
    }

    /**
     * Timer event fired when the underlying timer implementation has updated
     * the current stage. The current stage and remaining duration of the timer
     * is passed as a parameter.
     *
     * @param stage     the current stage the timer is running
     * @param remaining the amount of time remaining on the timer
     */
    @Override
    public void onStageUpdate(Duration stage, Duration remaining) {
    }

    /**
     * Timer event fired when the underlying timer implementation has ended
     * a stage. The stage the timer ended is passed as a parameter.
     *
     * @param stage the stage ended by the timer
     */
    public void onStageEnd(Duration stage) {
    }
}

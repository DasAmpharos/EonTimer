package com.github.dylmeadows.eontimer.handlers;

import java.time.Duration;

public interface TimerStageLifecycleListener {

    /**
     * Timer event fired when the underlying timer implementation has started
     * a stage. The stage the timer started is passed as a parameter.
     *
     * @param stage the stage started by the timer
     */
    void onStageStart(Duration stage);

    /**
     * Timer event fired when the underlying timer implementation has updated
     * the current stage. The current stage and remaining duration of the timer
     * is passed as a parameter.
     *
     * @param stage     the current stage the timer is running
     * @param remaining the amount of time remaining on the timer
     */
    void onStageUpdate(Duration stage, Duration remaining);

    /**
     * Timer event fired when the underlying timer implementation has ended
     * a stage. The stage the timer ended is passed as a parameter.
     *
     * @param stage the stage ended by the timer
     */
    void onStageEnd(Duration stage);
}

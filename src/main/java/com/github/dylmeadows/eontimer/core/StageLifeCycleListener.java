package com.github.dylmeadows.eontimer.core;

@SuppressWarnings("unused")
public interface StageLifeCycleListener {

    void onStageStart(long stage);

    void onStageUpdate(long stage, long remaining);

    void onStageEnd(long stage);
}

package com.github.dylmeadows.eontimer.timers;

public interface TimerCalculator<T extends Timer> {

    int MINIMUM_LENGTH = 14000;

    int[] getStages(T timer);

    int getMinutesBeforeTarget(T timer);

}

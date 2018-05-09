package com.github.dylmeadows.eontimer.handlers;

import com.github.dylmeadows.eontimer.timers.Timer;
import javafx.beans.value.ObservableValue;

public interface TimerChangeListener {

    void onTimerChange(ObservableValue<? extends Timer> observable, Timer oldValue, Timer newValue);
}

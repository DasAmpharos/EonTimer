package com.github.dylmeadows.eontimer.handlers;

import com.github.dylmeadows.eontimer.model.timer.Timer;
import javafx.beans.value.ObservableValue;

@SuppressWarnings("unused")
public interface TimerChangeListener {

    void onTimerChange(ObservableValue<? extends Timer> observable, Timer oldValue, Timer newValue);
}

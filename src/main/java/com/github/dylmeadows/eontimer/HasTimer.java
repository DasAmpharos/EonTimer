package com.github.dylmeadows.eontimer;

import com.github.dylmeadows.eontimer.timers.Timer;
import javafx.beans.property.ReadOnlyObjectProperty;

public interface HasTimer {

    Timer getTimer();

    ReadOnlyObjectProperty<Timer> timerProperty();
}

package com.github.dylmeadows.eontimer.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.springframework.stereotype.Component;

@Component
public class TimerMasterModel {

    private final ObjectProperty<Timer> timer = new SimpleObjectProperty<>(Timer.EMPTY_TIMER);

    public Timer getTimer() {
        return timer.get();
    }

    public ObjectProperty<Timer> timerProperty() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer.set(timer);
    }
}

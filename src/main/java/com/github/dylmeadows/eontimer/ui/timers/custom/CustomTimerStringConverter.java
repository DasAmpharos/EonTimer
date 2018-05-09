package com.github.dylmeadows.eontimer.ui.timers.custom;

import javafx.util.StringConverter;

import java.time.Duration;

class CustomTimerStringConverter extends StringConverter<Duration> {

    @Override
    public String toString(Duration object) {
        return Integer.toString((int) object.toMillis());
    }

    @Override
    public Duration fromString(String string) {
        int millis = Integer.parseInt(string);
        return Duration.ofMillis(millis);
    }
}

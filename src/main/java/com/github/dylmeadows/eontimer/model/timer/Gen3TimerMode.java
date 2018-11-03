package com.github.dylmeadows.eontimer.model.timer;

import com.github.dylmeadows.javafx.util.Option;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gen3TimerMode implements Option {
    STANDARD("Standard"),
    VARIABLE_TARGET("Variable Target");

    private final String text;
}

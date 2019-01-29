package io.github.dylmeadows.eontimer.model.timer;

import io.github.dylmeadows.common.javafx.util.Choice;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gen3TimerMode implements Choice {
    STANDARD("Standard"),
    VARIABLE_TARGET("Variable Target");

    private final String text;
}

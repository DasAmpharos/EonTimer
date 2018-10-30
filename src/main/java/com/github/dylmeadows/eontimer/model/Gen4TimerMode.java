package com.github.dylmeadows.eontimer.model;

import com.github.dylmeadows.javafx.util.Option;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gen4TimerMode implements Option {
    STANDARD("Standard");

    private final String text;
}

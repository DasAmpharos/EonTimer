package com.github.dylmeadows.eontimer.model;

import com.github.dylmeadows.javafx.util.Option;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum ThemeBackgroundMode implements Option {
    DEFAULT("Default"),
    COLOR("Color"),
    IMAGE("Image");

    private final String text;
}

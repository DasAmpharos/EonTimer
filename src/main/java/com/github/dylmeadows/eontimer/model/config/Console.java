package com.github.dylmeadows.eontimer.model.config;

import com.github.dylmeadows.javafx.util.Option;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum Console implements Option {
    GBA("GBA", 59.7271),
    NDS("NDS", 59.8261),
    DSI("DSI", 59.8261),
    _3DS("3DS", 59.8261);

    private final String text;
    private final double fps;

    public double getFrameRate() {
        return 1000 / fps;
    }
}

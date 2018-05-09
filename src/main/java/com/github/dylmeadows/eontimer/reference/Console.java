package com.github.dylmeadows.eontimer.reference;

import com.github.dylmeadows.common.util.LocalizedOption;
import com.github.dylmeadows.common.util.ResourceBundles;

public enum Console implements LocalizedOption {
    GBA(59.7271),
    NDS(59.8261),
    DSI(59.8261),
    _3DS(59.8261);

    private final double fps;

    private final double frameRate;

    Console(double fps) {
        this.fps = fps;
        this.frameRate = 1000 / fps;
    }

    public static Console findByLocalizedValue(String i18n) {
        return LocalizedOption.findByLocalizedValue(Console.class, i18n);
    }

    @Override
    public String getLocalizedValue() {
        return ResourceBundles.getBundle(Console.class).getString(name());
    }

    @Override
    public String toString() {
        return getLocalizedValue();
    }

    public double getFps() {
        return fps;
    }

    public double getFrameRate() {
        return frameRate;
    }
}

package com.github.dylmeadows.eontimer.model;

import com.github.dylmeadows.common.util.LocalizedEnum;
import com.github.dylmeadows.eontimer.util.ResourceBundles;

public enum Gen4TimerMode implements LocalizedEnum {
    STANDARD;

    @Override
    public String getLocalizedValue() {
        return ResourceBundles.getBundle(Gen4TimerMode.class).getString(name());
    }

    @Override
    public String toString() {
        return getLocalizedValue();
    }
}

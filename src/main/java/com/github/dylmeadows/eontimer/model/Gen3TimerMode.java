package com.github.dylmeadows.eontimer.model;

import com.github.dylmeadows.common.util.LocalizedEnum;
import com.github.dylmeadows.eontimer.util.ResourceBundles;

public enum Gen3TimerMode implements LocalizedEnum {
    STANDARD, VARIABLE_TARGET;

    @Override
    public String getLocalizedValue() {
        return ResourceBundles.getBundle(Gen3TimerMode.class).getString(name());
    }

    @Override
    public String toString() {
        return getLocalizedValue();
    }
}

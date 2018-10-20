package com.github.dylmeadows.eontimer.model;

import com.github.dylmeadows.common.util.LocalizedEnum;
import com.github.dylmeadows.eontimer.util.ResourceBundles;

public enum Gen5TimerMode implements LocalizedEnum {
    STANDARD, C_GEAR, ENTRALINK, ENHANCED_ENTRALINK;

    @Override
    public String getLocalizedValue() {
        return ResourceBundles.getBundle(Gen5TimerMode.class).getString(name());
    }

    @Override
    public String toString() {
        return getLocalizedValue();
    }
}

package com.github.dylmeadows.eontimer.reference;

import com.github.dylmeadows.common.util.LocalizedOption;
import com.github.dylmeadows.common.util.ResourceBundles;

public enum Gen3TimerMode implements LocalizedOption {
    STANDARD, VARIABLE_TARGET;

    public static Gen3TimerMode findByLocalizedValue(String i18n) {
        return LocalizedOption.findByLocalizedValue(Gen3TimerMode.class, i18n);
    }

    @Override
    public String getLocalizedValue() {
        return ResourceBundles.getBundle(Gen3TimerMode.class).getString(name());
    }

    @Override
    public String toString() {
        return getLocalizedValue();
    }
}

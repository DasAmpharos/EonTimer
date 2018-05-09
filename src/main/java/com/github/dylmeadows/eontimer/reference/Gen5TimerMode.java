package com.github.dylmeadows.eontimer.reference;

import com.github.dylmeadows.common.util.LocalizedOption;
import com.github.dylmeadows.common.util.ResourceBundles;

public enum Gen5TimerMode implements LocalizedOption {
    STANDARD, C_GEAR, ENTRALINK, ENTRALINK_PLUS;

    public static Gen5TimerMode findByLocalizedValue(String i18n) {
        return LocalizedOption.findByLocalizedValue(Gen5TimerMode.class, i18n);
    }

    @Override
    public String getLocalizedValue() {
        return ResourceBundles.getBundle(Gen5TimerMode.class).getString(name());
    }

    @Override
    public String toString() {
        return getLocalizedValue();
    }
}

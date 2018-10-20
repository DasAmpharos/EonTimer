package com.github.dylmeadows.eontimer.model;

import com.github.dylmeadows.common.util.LocalizedEnum;
import com.github.dylmeadows.eontimer.util.ResourceBundles;

public enum ActionMode implements LocalizedEnum {
    AUDIO, VISUAL, AV, NONE;

    @Override
    public String getLocalizedValue() {
        return ResourceBundles.getBundle(ActionMode.class).getString(name());
    }

    @Override
    public String toString() {
        return getLocalizedValue();
    }
}

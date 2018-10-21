package com.github.dylmeadows.eontimer.util.extension;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ArrayExtensions {

    @SafeVarargs
    public <T> T[] arrayOf(T... values) {
        return values;
    }
}

package com.github.dylmeadows.eontimer.util;

public final class Value<T> {

    private T value;

    public Value() {
        this.value = null;
    }

    public Value(T initialValue) {
        this.value = initialValue;
    }

    public final T get() {
        return value;
    }

    public final void set(T value) {
        this.value = value;
    }
}

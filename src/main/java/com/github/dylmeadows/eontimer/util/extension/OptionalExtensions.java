package com.github.dylmeadows.eontimer.util.extension;

import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.function.Consumer;

@UtilityClass
public class OptionalExtensions {

    public <T> void ifPresentOrElse(Optional<T> optional, Consumer<T> presentAction, Runnable emptyAction) {
        if (optional.isPresent()) {
            optional.ifPresent(presentAction);
        } else {
            emptyAction.run();
        }
    }

    public <T> void ifPresentOrElse(T t, Consumer<T> presentAction, Runnable emptyAction) {
        ifPresentOrElse(Optional.ofNullable(t), presentAction, emptyAction);
    }
}

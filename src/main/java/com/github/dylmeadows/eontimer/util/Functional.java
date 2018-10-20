package com.github.dylmeadows.eontimer.util;

import lombok.experimental.UtilityClass;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@UtilityClass
public class Functional {

    public <T> Consumer<T> asConsumer(Consumer<T> consumer) {
        return consumer;
    }

    public <T> Predicate<T> asPredicate(Predicate<T> predicate) {
        return predicate;
    }

    public <T, R> Function<T, R> asFunction(Function<T, R> function) {
        return function;
    }

    public <T> Predicate<T> negate(Predicate<T> predicate) {
        return predicate.negate();
    }
}

package io.github.dylmeadows.eontimer.util

val <T : Enum<T>> Enum<T>.properName: String
    get() {
        return name.split('_')
            .map(String::toLowerCase)
            .joinToString(" ",
                transform = String::capitalize)
    }
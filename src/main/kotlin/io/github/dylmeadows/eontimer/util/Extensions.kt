package io.github.dylmeadows.eontimer.util

fun <T : Any, U : Any> T.transform(mapper: (T) -> U): U {
    return mapper(this)
}

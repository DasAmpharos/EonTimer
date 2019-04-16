package io.github.dylmeadows.eontimer.util

import kotlinx.coroutines.channels.Channel

fun <T : Any, U : Any> T.transform(mapper: (T) -> U): U {
    return mapper(this)
}

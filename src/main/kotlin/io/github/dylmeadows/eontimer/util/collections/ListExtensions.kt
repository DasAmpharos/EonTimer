package io.github.dylmeadows.eontimer.util.collections

import java.util.*

fun <T> List<T>.toUnmodifiableList(): List<T> =
        Collections.unmodifiableList(this)
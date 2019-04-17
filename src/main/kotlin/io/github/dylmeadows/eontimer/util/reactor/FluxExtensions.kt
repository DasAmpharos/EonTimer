package io.github.dylmeadows.eontimer.util.reactor

import reactor.core.publisher.Flux
import java.time.Duration

fun Flux<Long>.timeInterval(): Flux<Duration> {
    return elapsed().map { it.t1 }
        .map(Duration::ofMillis)
}

fun <T> Flux<T>.changesOf(): Flux<Change<T>> {
    var previousValue: T? = null
    return map { Change(previousValue, it) }
        .doOnNext { previousValue = it.newValue }
}

data class Change<T>(val oldValue: T?, val newValue: T?)
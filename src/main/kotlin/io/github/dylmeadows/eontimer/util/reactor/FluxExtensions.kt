package io.github.dylmeadows.eontimer.util.reactor

import reactor.core.publisher.Flux

fun Flux<Long>.timeInterval(): Flux<Long> {
    return elapsed().map { it.t1 }
}
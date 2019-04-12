package io.github.dylmeadows.eontimer.util.reactor

import reactor.core.publisher.Flux
import java.time.Duration

fun Flux<Long>.timeInterval(): Flux<Duration> {
    return elapsed()
        .map { it.t1 }
        .map(Duration::ofMillis)
}
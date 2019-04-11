package io.github.dylmeadows.eontimer.util.reactor

import reactor.core.publisher.Flux
import java.time.Duration

object FluxFactory {

    fun timer(period: Duration): Flux<TimerTick> {
        return timeInterval(period)
            .scan(TimerTick.ZERO, TimerTick::plus)
    }

    fun timer(period: Duration, duration: Duration): Flux<TimerTick> {
        return timer(period)
            .takeUntil { tick ->
                tick.elapsed >= duration.toMillis()
            }
    }

    fun timeInterval(period: Duration): Flux<Long> {
        return Flux.interval(period).timeInterval()
    }
}
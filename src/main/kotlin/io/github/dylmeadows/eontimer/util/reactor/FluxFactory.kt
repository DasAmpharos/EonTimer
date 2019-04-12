package io.github.dylmeadows.eontimer.util.reactor

import io.github.dylmeadows.eontimer.util.INDEFINITE
import reactor.core.publisher.Flux
import java.time.Duration

object FluxFactory {

    fun timer(period: Duration): Flux<TimerState> {
        return timeInterval(period)
            .scan(TimerState(duration = INDEFINITE), TimerState::plus)
    }

    fun timer(period: Duration, duration: Duration): Flux<TimerState> {
        return timeInterval(period)
            .scan(TimerState(duration = duration), TimerState::plus)
            .takeUntil { state ->
                state.remaining <= Duration.ZERO
            }
    }

    fun multiTimer(period: Duration, durations: List<Duration>): Flux<TimerState> {
        return if (durations.isNotEmpty()) {
            Flux.concat(durations
                .map { duration ->
                    timer(period, duration)
                        .doOnNext { /* emit */ }
                })
        } else {
            Flux.empty()
        }
    }

    fun timeInterval(period: Duration): Flux<Duration> {
        return Flux.interval(period).timeInterval()
    }
}
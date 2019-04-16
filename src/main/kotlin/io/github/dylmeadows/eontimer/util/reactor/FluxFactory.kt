package io.github.dylmeadows.eontimer.util.reactor

import io.github.dylmeadows.eontimer.util.INDEFINITE
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import java.time.Duration
import java.time.Instant
import java.util.*

object FluxFactory {

    fun timer(period: Duration): Flux<TimerState> {
        return timer(period, INDEFINITE)
    }

    fun timer(period: Duration, duration: Duration): Flux<TimerState> {
        return timer(period, Collections.singletonList(duration))
    }

    fun timer(period: Duration, durations: List<Duration>): Flux<TimerState> {
        return if (durations.isNotEmpty()) {
            return Flux.create { emitter ->
                val job = GlobalScope.launch {
                    emitter.asTimer(period, durations)
                }
                emitter.onDispose(job::cancel)
                emitter.onCancel(job::cancel)
            }
        } else {
            Flux.empty()
        }
    }

    fun timeInterval(period: Duration): Flux<Duration> {
        return Flux.interval(period).timeInterval()
    }
}
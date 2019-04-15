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
            return Flux.create(asTimer(period, durations))
        } else {
            Flux.empty()
        }
    }

    fun asTimer(period: Duration, durations: List<Duration>): (FluxSink<TimerState>) -> Unit {
        return { emitter ->
            val job = GlobalScope.launch {
                var elapsed = Duration.ZERO
                durations.forEach { duration ->
                    var delay = period
                    var lastTimestamp = Instant.now()
                    while (elapsed < duration) {
                        delay(delay.toMillis())

                        val now = Instant.now()
                        val delta = Duration.between(lastTimestamp, now)
                        // adjust delay
                        if ((duration - elapsed) < period) {
                            delay = duration - elapsed
                        } else {
                            delay -= delta - period
                        }
                        lastTimestamp = now
                        elapsed += delta

                        emitter.next(TimerState(delta, elapsed, duration))
                    }
                    elapsed -= duration
                }
                emitter.complete()
            }
            emitter.onDispose(job::cancel)
            emitter.onCancel(job::cancel)
        }
    }

    fun timeInterval(period: Duration): Flux<Duration> {
        return Flux.interval(period).timeInterval()
    }
}